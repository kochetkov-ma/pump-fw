package ru.mk.pump.cucumber.steps.hooks;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.cucumber.CucumberUtil;

@Slf4j
public class WebHooks {

    @Setter
    private static boolean beforeScenarioHook = CucumberCore.instance().getConfig().isBeforeScenarioHook();

    @Setter
    private static boolean afterScenarioHook = CucumberCore.instance().getConfig().isAfterScenarioHook();

    @Before(order = 0)
    public void mainHook() {
        CucumberCore.instance().getMonitor().checkPlugin();
    }

    @Before
    public void beforeScenarioDefault(Scenario scenario) {
        if (beforeScenarioHook) {
            log.info("[HOOK] Before Scenario" + Strings.line() + CucumberUtil.toPrettyString(scenario) + Strings.line());
        }
    }

    @After
    public void afterScenarioDefault(Scenario scenario) {
        if (afterScenarioHook) {
            log.info("[HOOK] After Scenario" + Strings.line() + CucumberUtil.toPrettyString(scenario) + Strings.line());
        }
    }
}