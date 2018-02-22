package ru.mk.pump.cucumber;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.config.ConfigurationHelper;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.cucumber.plugin.CucumberMonitor;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.common.WebItemsController;
import ru.mk.pump.web.component.ComponentStaticManager;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.interpretator.rules.Pumpkin;
import ru.mk.pump.web.page.PageManager;
import ru.mk.pump.web.utils.TestVars;
import ru.mk.pump.web.utils.WebReporter;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CucumberPumpCore {

    private static final String DEFAULT_SYSTEM_ENV_NAME = "pump.cucumber.configuration.path";

    private static final String DEFAULT_CLASSPATH_RESOURCE = "pump.cucumber.properties";

    private static final CucumberPumpCore INSTANCE = new CucumberPumpCore();

    private final Map<String, Object> DEFAULT_TEST_VARS = ImmutableMap.<String, Object>builder()
        .put("test_runner", "cucumber")
        .build();

    private final ConfigurationHelper<CucumberConfig> configHelper = new ConfigurationHelper<>(DEFAULT_SYSTEM_ENV_NAME, DEFAULT_CLASSPATH_RESOURCE,
        CucumberConfig.of());

    private final ThreadLocal<CucumberMonitor> cucumberMonitor = InheritableThreadLocal.withInitial(CucumberMonitor::new);

    @Getter
    private final Browsers browsers = new Browsers();

    @Getter
    private final TestVars testVariables = TestVars.of(DEFAULT_TEST_VARS);

    private volatile WebItemsController webController;


    public static CucumberPumpCore instance() {
        return INSTANCE;
    }

    public CucumberMonitor getMonitor() {
        return cucumberMonitor.get();
    }

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
        if (webController == null) {
            ComponentStaticManager cManager = new ComponentStaticManager(browsers, ConfigurationHolder.get().getComponent().getPackages());
            PageManager pManager = new PageManager(browsers, ConfigurationHolder.get().getPage().getPackages());
            INSTANCE.webController = new WebItemsController(pManager, cManager, testVariables);
        }
        return webController;
    }

    public Pumpkin paramParser() {
        return Pumpkin.newParamParser(testVariables.asMap());
    }

    public synchronized CucumberConfig getConfig() {
        if (configHelper.getActualConfig() == null) {
            return configHelper.loadAuto();
        }
        return configHelper.getActualConfig();
    }
}