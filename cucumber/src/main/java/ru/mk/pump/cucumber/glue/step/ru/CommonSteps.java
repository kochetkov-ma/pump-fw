package ru.mk.pump.cucumber.glue.step.ru;

import cucumber.api.java.en.Given;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.cucumber.glue.AbstractSteps;

public class CommonSteps extends AbstractSteps {

    @Given("^глобальные переменные сохранено для (.+?) значение (.+?)$")
    public void saveTestVar(String key, Object value) {
        core().getTestVariables().put(key, value);
    }

    @Given("^глобальные переменные сохранено для (.+?) результат выполнения (.+?)$")
    public void evaluateAndSaveTestVar(String key, String pumpkinExpression) {
        core().getTestVariables().put(key, transform(pumpkinExpression));
    }

    @Given("^ничего не проиходит '(.+)' секунд")
    public void wait(int sec) {
        Waiter.sleep(sec * 1000);
    }
}