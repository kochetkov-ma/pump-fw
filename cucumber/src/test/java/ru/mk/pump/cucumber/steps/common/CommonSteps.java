package ru.mk.pump.cucumber.steps.common;

import cucumber.api.java.en.Given;
import ru.mk.pump.cucumber.steps.AbstractSteps;
import ru.mk.pump.cucumber.transform.PParam;

public class CommonSteps extends AbstractSteps {

    @Given("^Common - save to var '(.+)' new value '(.+)'$")
    public void saveTestVar(String key, @PParam Object value) {
        core().getTestVariables().put(key, value);
    }

    @Given("^Common - save to var '(.+)' method result '(.+)'$")
    public void evaluateAndSaveTestVar(String key, String pumpkinExpression) {
        core().getTestVariables().put(key, transform(pumpkinExpression));
    }
}
