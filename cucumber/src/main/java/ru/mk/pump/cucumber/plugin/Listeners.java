package ru.mk.pump.cucumber.plugin;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.experimental.UtilityClass;
import ru.mk.pump.cucumber.CucumberConfig;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.web.configuration.ConfigurationHolder;

@UtilityClass
class Listeners {

    Set<CucumberListener> defaultListeners(CucumberConfig cucumberConfig) {
        ImmutableSet.Builder<CucumberListener> builder = ImmutableSet.builder();
        if (cucumberConfig.isBrowserManager()) {
            builder.add(new BrowserManager(CucumberCore.instance().getBrowsers(), ConfigurationHolder.get().getBrowserConfig(),
                CucumberCore.instance().getConfig().isOneBrowserAllFeature()));
        }
        if (cucumberConfig.isInformationManager()) {
            builder.add(new InformationManager(CucumberCore.instance()));
        }
        return builder.build();
    }
}