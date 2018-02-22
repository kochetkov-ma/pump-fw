package ru.mk.pump.cucumber;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.config.Property;

@Getter
@NoArgsConstructor
public class CucumberConfig {

    @Property("hook.before.scenario")
    private boolean beforeScenarioHook = true;

    @Property("hook.after.scenario")
    private boolean afterScenarioHook = true;

    @Property("hook.after.feature")
    private boolean afterFeatureHook = true;

    public static CucumberConfig of() {
        CucumberConfig res = new CucumberConfig();
        return res;
    }

}
