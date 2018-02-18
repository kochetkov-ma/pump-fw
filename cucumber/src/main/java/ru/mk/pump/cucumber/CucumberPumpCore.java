package ru.mk.pump.cucumber;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.common.WebItemsController;
import ru.mk.pump.web.component.ComponentStaticManager;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.page.PageManager;
import ru.mk.pump.web.utils.TestVars;
import ru.mk.pump.web.utils.WebReporter;

import java.util.Map;

@SuppressWarnings("unused")
@UtilityClass
public class CucumberPumpCore {

    private final Map<String, Object> DEFAULT_TEST_VARS = ImmutableMap.<String, Object>builder()
            .put("test_runner", "cucumber")
            .build();

    @Getter
    private final Browsers browsers = new Browsers();

    @Getter
    private final TestVars testVariables = TestVars.of(DEFAULT_TEST_VARS);

    private WebItemsController webController;

    public Reporter getReporter() {
        return WebReporter.getReporter();
    }

    public Verifier getVerifier() {
        return WebReporter.getVerifier();
    }

    public WebItemsController getWebController() {
        if (!browsers.has()) {
            throw new IllegalStateException("No one browser had not started. Start browser before using WebController");
        }
        ComponentStaticManager cManager = new ComponentStaticManager(browsers, ConfigurationHolder.get().getComponent().getPackages());
        PageManager pManager = new PageManager(browsers, ConfigurationHolder.get().getPage().getPackages());
        CucumberPumpCore.webController = new WebItemsController(pManager, cManager, testVariables);
        return webController;
    }
}