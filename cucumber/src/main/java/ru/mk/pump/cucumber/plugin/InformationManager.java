package ru.mk.pump.cucumber.plugin;

import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.web.configuration.ConfigurationHolder;

public class InformationManager implements CucumberListener {

    private final Reporter reporter;
    private CucumberCore cucumberCore;

    public InformationManager(CucumberCore cucumberCore) {
        this.cucumberCore = cucumberCore;
        this.reporter = cucumberCore.getReporter();
    }

    @Override
    public void onStartTest(CucumberMonitor monitor) {
        if (ConfigurationHolder.instance().getConfigurationHelper().getLoader().isPresent()) {
            reporter.info("Framework configuration", ConfigurationHolder.instance().getConfigurationHelper().getLoader().get().getHistory().toPrettyString());
        } else {
            reporter.info("Pump configuration", Strings.toString(ConfigurationHolder.instance().getConfigurationHelper().getActualConfig()));
        }
        if (cucumberCore.getConfigHelper().getLoader().isPresent()) {
            reporter.info("Cucumber configuration", cucumberCore.getConfigHelper().getLoader().get().getHistory().toPrettyString());
        } else {
            reporter.info("Cucumber configuration", Strings.toString(cucumberCore.getConfigHelper().getActualConfig()));
        }
        reporter.info("Stand configuration", "not implemented yet");
    }

    @Override
    public void onStartFeature(CucumberMonitor monitor) {
        monitor.getLastFeature().ifPresent((feature) -> reporter.info("Feature started", feature.toPrettyString()));
    }

    @Override
    public void onStartScenario(CucumberMonitor monitor) {
        monitor.getLastFeature().ifPresent(feature -> feature.getActiveScenario().ifPresent(scenario -> reporter.info("Scenario started", scenario.toPrettyString())));
    }

    @Override
    public void onFinishTest(CucumberMonitor monitor) {
        reporter.info("Test Variables", cucumberCore.getTestVariables().toPrettyString());
    }

    @Override
    public void onFinishFeature(CucumberMonitor monitor) {
        monitor.getLastFeature().ifPresent((feature) -> reporter.info("Feature finished", feature.toPrettyString()));
    }

    @Override
    public void onFinishScenario(CucumberMonitor monitor) {
        monitor.getLastFeature().ifPresent(feature -> feature.getLastExecutedScenario().ifPresent(scenario -> reporter.info("Scenario finished", scenario.toPrettyString())));
    }
}