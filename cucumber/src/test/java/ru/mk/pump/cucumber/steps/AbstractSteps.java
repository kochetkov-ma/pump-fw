package ru.mk.pump.cucumber.steps;

import ru.mk.pump.cucumber.CucumberPumpCore;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractSteps {

    private final CucumberPumpCore core;

    public AbstractSteps() {
        core = CucumberPumpCore.instance();
    }

    public CucumberPumpCore core() {
        return core;
    }

    public Object transform(String pumpkinExpression) {
        return core.getWebController().execute(pumpkinExpression);
    }

}