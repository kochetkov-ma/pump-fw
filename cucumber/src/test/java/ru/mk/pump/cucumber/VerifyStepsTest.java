package ru.mk.pump.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.runtime.Runtime;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
    strict = true,
    plugin = "io.qameta.allure.cucumber2jvm.AllureCucumber2Jvm",
    glue = "ru.mk.pump.cucumber",
    features = "classpath:features/test.feature")
public class VerifyStepsTest {
    void method(){

    }
}