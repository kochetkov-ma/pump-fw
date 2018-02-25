package ru.mk.pump.cucumber.steps;

import ru.mk.pump.cucumber.CucumberCore;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractSteps {

    private final CucumberCore core;

    public AbstractSteps() {
        core = CucumberCore.instance();
    }

    public CucumberCore core() {
        return core;
    }

    public Object transform(String pumpkinExpression) {
        return core.getWebController().execute(pumpkinExpression);
    }

}