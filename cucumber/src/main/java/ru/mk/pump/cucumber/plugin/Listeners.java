package ru.mk.pump.cucumber.plugin;

import com.google.common.collect.ImmutableSet;
import lombok.experimental.UtilityClass;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.web.configuration.ConfigurationHolder;

import java.util.Set;

@UtilityClass
class Listeners {

    Set<CucumberListener> defaultListeners() {
        return ImmutableSet.<CucumberListener>builder()
                .add(new BrowserManager(CucumberCore.instance().getBrowsers(), ConfigurationHolder.get().getBrowserConfig(), CucumberCore.instance().getConfig().isOneBrowserAllFeature()))
                .add(new InformationManager(CucumberCore.instance()))
                .build();
    }
}