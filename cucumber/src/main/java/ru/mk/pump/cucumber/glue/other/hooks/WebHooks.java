package ru.mk.pump.cucumber.glue.other.hooks;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.qameta.allure.Step;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.DesktopScreenshoter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.cucumber.CucumberUtil;
import ru.mk.pump.web.configuration.ConfigurationHolder;

@Slf4j
public class WebHooks {

    final static private DesktopScreenshoter DESKTOP_SCREENSHOTER = new DesktopScreenshoter();

    @Setter
    private static boolean beforeScenarioHook = CucumberCore.instance().getConfig().isBeforeScenarioHook();

    @Setter
    private static boolean afterScenarioHook = CucumberCore.instance().getConfig().isAfterScenarioHook();

    private final CucumberCore core;

    public WebHooks() {
        this.core = CucumberCore.instance();
    }

    @Before(order = 10)
    @Step("Before scenario '{scenario.testCase.pickleEvent.pickle.name}'")
    public void beforeScenarioDefault(Scenario scenario) {
        final TagHelper tags = new TagHelper(scenario);
        checkPluginHook();
        skipHook(tags);
        if (beforeScenarioHook) {
            if (tags.isBrowserRestart()) {
                browserRestart();
            }
            screenHook("start");
            log.info("[HOOK] Before Scenario" + Strings.line() + CucumberUtil.toPrettyString(scenario) + Strings.line());
        }
    }

    @After(order = 10)
    @Step("After scenario '{scenario.testCase.pickleEvent.pickle.name}'")
    public void afterScenarioDefault(Scenario scenario) {
        if (afterScenarioHook) {
            screenHook("finish");
            log.info("[HOOK] After Scenario" + Strings.line() + CucumberUtil.toPrettyString(scenario) + Strings.line());
        }
    }

    private void checkPluginHook() {
        core.getMonitor().checkPlugin();
    }

    private void screenHook(String status) {
        core.getReporter()
                .attachments().screen("On scenario " + status,
                () -> DESKTOP_SCREENSHOTER.getScreen().orElse(null));


    }

    private void skipHook(TagHelper tagHelper) {
        if (tagHelper.isSkip()) {
            CucumberUtil.skipScenario(tagHelper.getScenario());
        } else {
            core.getMonitor().getLastFeature().ifPresent(feature -> {
                if (!feature.isOk() && !tagHelper.isNoSkip()) {
                    CucumberUtil.skipScenario(tagHelper.getScenario());
                }
            });
        }
    }

    private void browserRestart() {
        core.getBrowsers().closeCurrentThread();
        core.getBrowsers().newBrowser(ConfigurationHolder.get().getBrowserConfig());
    }
}