package ru.mk.pump.cucumber.steps.hooks;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.cucumber.CucumberPumpCore;
import ru.mk.pump.cucumber.CucumberUtil;

@Slf4j
public class WebHooks {

    @Setter
    private static boolean beforeScenarioHook = CucumberPumpCore.instance().getConfig().isBeforeScenarioHook();

    @Setter
    private static boolean afterScenarioHook = CucumberPumpCore.instance().getConfig().isAfterScenarioHook();

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