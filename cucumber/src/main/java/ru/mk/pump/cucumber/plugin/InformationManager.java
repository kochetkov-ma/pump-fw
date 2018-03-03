package ru.mk.pump.cucumber.plugin;

import io.qameta.allure.util.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.FileUtils;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.web.configuration.ConfigurationHolder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
@Slf4j
public class InformationManager implements CucumberListener {

    private final Reporter reporter;
    private CucumberCore cucumberCore;

    InformationManager(CucumberCore cucumberCore) {
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
        /*nothing*/
    }

    @Override
    public void onFinishTest(CucumberMonitor monitor) {
        reporter.info("Test Variables", cucumberCore.getTestVariables().toPrettyString());
        copyStandConfig();
    }

    @Override
    public void onFinishFeature(CucumberMonitor monitor) {
        /*nothing*/
    }

    @Override
    public void onFinishScenario(CucumberMonitor monitor) {
        /*nothing*/
    }

    private void copyStandConfig() {
        try {
            if (cucumberCore.getStandConfig() != null) {
                final Properties properties = PropertiesUtils.loadAllureProperties();
                final Path resultDir = Paths.get(properties.getProperty("allure.results.directory", "allure-results"));
                final Path source = cucumberCore.getStandConfig().getPropertiesFile();
                FileUtils.copy(source, resultDir.resolve("environment.properties"));
            }
        } catch (Exception ex){
            log.error("Cannot copy environment.properties", ex);
        }
        log.warn("Environment properties (Stand.class) in CucumberCore.class was not defined");
    }
}