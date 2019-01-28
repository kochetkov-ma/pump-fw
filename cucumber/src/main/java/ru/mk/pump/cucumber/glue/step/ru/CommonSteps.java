package ru.mk.pump.cucumber.glue.step.ru;

import cucumber.api.java.en.Given;

import org.apache.commons.lang3.RandomUtils;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.cucumber.glue.AbstractSteps;

public class CommonSteps extends AbstractSteps {

    @Given("^глобальная переменная (.+?) со строкой (.+?) сохранена$")
    public void saveStringTestVar(String key, String value) {
        core().getTestVariables().put(key, value);
    }

    @Given("^глобальная переменная (.+?) с числом (.+?) сохранена$")
    public void saveLongTestVar(String key, long value) {
        core().getTestVariables().put(key, value);
    }

    @Given("^глобальная переменная (.+?) с объектом (.+?) сохранена$")
    public void saveObjectTestVar(String key, String pumpkinObject) {
        core().getTestVariables().put(key, core().getWebController().execute(pumpkinObject));
    }

    @Given("^глобальная переменная (.+?) со случайным числом от ([0-9]+?) до ([0-9]+?) сохранена$")
    public void saveObjectTestVar(String key, int from, int to) {
        core().getTestVariables().put(key, RandomUtils.nextLong(from, to));
    }

    @Given("^ничего не проиходит ([0-9]+?) секунд$")
    public void wait(int sec) {
        Waiter.sleep(sec * 1000);
    }
}