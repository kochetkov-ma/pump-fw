package ru.mk.pump.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import java.util.Optional;
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


@RunWith(Cucumber.class)
@CucumberOptions(
        strict = true,
        plugin = {"ru.mk.pump.cucumber.plugin.PumpCucumberPlugin"},
        glue = "ru.mk.pump.cucumber",
        features = "classpath:features/")
public class VerifyStepsTest {

    @PPage(value = "Главная страница", desc = "Главная страница", baseUrl = "https://ipotekaonline.open.ru")
    public static class MainPage extends BasePage {

        @FindBy(tagName = "h2")
        @PElement(value = "Заголовок", desc = "Главный заголовок страницы")
        @Getter
        private TextArea pageTitle;

        public MainPage(@NonNull Browser browser) {
            super(browser);
        }

        @Override
        public Optional<Element> getTitle() {
            return Optional.of(pageTitle);
        }
    }
}