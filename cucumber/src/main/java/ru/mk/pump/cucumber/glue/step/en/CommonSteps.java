package ru.mk.pump.cucumber.glue.step.en;

import cucumber.api.java.en.Given;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.cucumber.glue.AbstractSteps;

public class CommonSteps extends AbstractSteps {

    @Given("^Common - save to var '(.+)' new value '(.+)'$")
    public void saveTestVar(String key, Object value) {
        core().getTestVariables().put(key, value);
    }

    @Given("^Common - save to var '(.+)' method result '(.+)'$")
    public void evaluateAndSaveTestVar(String key, String pumpkinExpression) {
        core().getTestVariables().put(key, transform(pumpkinExpression));
    }

    @Given("^Common - wait '(.+)' sec$")
    public void wait(int sec) {
        Waiter.sleep(sec * 1000);
    }
}
