package ru.mk.pump.cucumber;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

import java.util.Map;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CucumberCore extends AbstractModule {

    private static final String DEFAULT_SYSTEM_ENV_NAME = "pump.cucumber.configuration.path";

    private static final String DEFAULT_CLASSPATH_RESOURCE = "pump.cucumber.properties";

    private static final CucumberCore INSTANCE = new CucumberCore();

    private final Map<String, Object> DEFAULT_TEST_VARS = ImmutableMap.<String, Object>builder()
            .put("test_runner", "cucumber")
            .build();

    @Setter
    @Getter(onMethod_ = {@Provides})
    private Stand standConfig;

    @Getter
    private final ConfigurationHelper<CucumberConfig> configHelper = new ConfigurationHelper<>(DEFAULT_SYSTEM_ENV_NAME, DEFAULT_CLASSPATH_RESOURCE,
            CucumberConfig.of());

    private final ThreadLocal<CucumberMonitor> cucumberMonitor = InheritableThreadLocal.withInitial(CucumberMonitor::newInactive);

    @Getter(onMethod_ = {@Provides})
    private final Browsers browsers = new Browsers();

    @Getter(onMethod_ = {@Provides})
    private final TestVars testVariables = TestVars.of(DEFAULT_TEST_VARS);

    private volatile WebItemsController webController;

    public static CucumberCore instance() {
        return INSTANCE;
    }

    @Provides
    public CucumberMonitor getMonitor() {
        return cucumberMonitor.get();
    }

    /**
     * For internal using
     * @param monitor CucumberMonitor
     */
    public void setMonitor(CucumberMonitor monitor) {
        cucumberMonitor.set(monitor);
    }

    @Provides
    public Reporter getReporter() {
        return WebReporter.getReporter();
    }

    @Provides
    public Verifier getVerifier() {
        return WebReporter.getVerifier();
    }

    @Provides
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

    @Provides
    public Pumpkin paramParser() {
        return Pumpkin.newParamParser(testVariables.asMap());
    }

    @Provides
    public synchronized CucumberConfig getConfig() {
        if (configHelper.getActualConfig() == null) {
            return configHelper.loadAuto();
        }
        return configHelper.getActualConfig();
    }
}