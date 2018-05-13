package ru.mk.pump.cucumber.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;
import ru.mk.pump.commons.utils.ReflectionUtils;
import ru.mk.pump.cucumber.CucumberCore;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class PumpGuiceSource implements InjectorSource {

    @Override
    public Injector getInjector() {
        return Guice.createInjector(Stage.PRODUCTION, allModules());
    }

    private Collection<Module> allModules() {
        Collection<Module> res = prepareExtraModules();
        res.add(CucumberModules.SCENARIO);
        res.add(CucumberCore.instance());
        return res;
    }

    private Collection<Module> prepareExtraModules() {
        return Arrays.stream(CucumberCore.instance().getConfig().getGuiceModules())
                .map(cl -> ReflectionUtils.<Module>newInstance(cl))
                .collect(Collectors.toList());
    }
}
