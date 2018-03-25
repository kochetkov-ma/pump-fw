package ru.mk.pump.web.appium;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Getter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.AbstractBrowser;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.browsers.builders.AndroidAppDriverBuilder;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.common.api.annotations.PPage;
import ru.mk.pump.web.common.api.annotations.PString;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.page.BasePage;
import ru.mk.pump.web.page.PageLoaderPump;
import ru.mk.pump.web.utils.WebReporter;

import java.util.Observable;
import java.util.UUID;

public class AndroidAppTest {

    @PPage(value = "Калькулятор", desc = "Калькулятор")
    public static class Calculator extends BasePage {

        @FindBy(id = "com.android.calculator2:id/digit_1")
        @PElement(value = "Один")
        @PString(name = "focusCustomScript", value = "")
        @Getter
        private Button one;

        @FindBy(id = "com.android.calculator2:id/digit_2")
        @PElement(value = "Два")
        @PString(name = "focusCustomScript", value = "")
        @Getter
        private Button two;

        public Calculator(Browser browser) {
            super(browser);
            setPageLoader(new PageLoaderPump(this, WebReporter.getVerifier()));
        }

        @Override
        public String getTitle() {
            return "Калькулятор";
        }
    }

    @Test
    @Disabled
    void testAndroidCalculator() {
        ConfigurationHolder.init("pump-android.properties");

        Browser androidApp = new AbstractBrowser(new AndroidAppDriverBuilder(ConfigurationHolder.get().getBrowserConfig()), UUID.randomUUID().toString()) {
        };
        ((Observable) androidApp).deleteObserver(androidApp.windows());

        Browsers browsers = new Browsers();
        browsers.setBrowser(androidApp);
        browsers.get().start();
        Calculator calculator = new Calculator(browsers.get());
        calculator.getOne().click();
        calculator.getTwo().click();
    }

}
