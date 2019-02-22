package ru.mk.pump.cucumber.glue.step.ru;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import lombok.Getter;
import lombok.NonNull;
import org.junit.runner.RunWith;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.TextArea;
import ru.mk.pump.web.page.BasePage;

import java.util.Optional;


@RunWith(Cucumber.class)
@CucumberOptions(
        strict = true,
        plugin = {"ru.mk.pump.cucumber.plugin.PumpCucumberPlugin"},
        glue = {"ru.mk.pump.cucumber.glue.step.ru", "ru.mk.pump.cucumber.glue.other"},
        features = "classpath:features/ru-verify-steps-test-ru.feature")
public class VerifyStepsTest {


}