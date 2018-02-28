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

    @Property("plugin.enable")
    private boolean loadPumpPlugin = true;

    @Property("plugin.browser")
    private boolean browserManager = true;

    @Property("plugin.browser.singleton")
    private boolean oneBrowserAllFeature = true;

    @Property("plugin.information")
    private boolean informationManager = true;


    public static CucumberConfig of() {
        CucumberConfig res = new CucumberConfig();
        return res;
    }

}
