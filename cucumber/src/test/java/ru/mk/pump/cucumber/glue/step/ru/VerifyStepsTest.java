package ru.mk.pump.cucumber.glue.step.ru;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        strict = true,
        plugin = {"ru.mk.pump.cucumber.plugin.PumpCucumberPlugin"},
        glue = {"ru.mk.pump.cucumber.glue.step.ru", "ru.mk.pump.cucumber.glue.other"},
        features = "classpath:features/ru-verify-steps-test-ru.feature")
public class VerifyStepsTest {


}