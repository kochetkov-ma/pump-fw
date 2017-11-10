package ru.mk.pump.commons.reporter;

import io.qameta.allure.junit4.AllureJunit4;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class AllureRunner extends BlockJUnit4ClassRunner {

    public AllureRunner(Class<?> klass) throws InitializationError {
        super(klass);
        if (System.getProperty("allure.results.directory") == null) {
            System.setProperty("allure.results.directory", "out/allure-result");
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new AllureJunit4());
        notifier.fireTestRunStarted(getDescription());
        super.run(notifier);
    }
}
