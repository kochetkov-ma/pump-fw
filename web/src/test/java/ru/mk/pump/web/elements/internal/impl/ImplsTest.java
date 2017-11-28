package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Wait;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.internal.BaseElement;

@Slf4j
public class ImplsTest {

    private Browsers browsers;

    private BrowserConfig config;

    private Browser browser;

    @Before
    public void setUp(){
        browsers = new Browsers();
        config = new BrowserConfig(false, Size.of(true), BrowserType.CHROME);
        config.setWebDriverPath(ProjectResources.findResource("chromedriver.exe").toString());
        browser = browsers.newBrowser(config);
    }


    @Test
    public void test() {
        final BaseElement div = new ButtonImpl(By.xpath(".//div[contains(@class,'b-calculator__rightblock')]"), browser);
        final BaseElement section = new ButtonImpl(By.tagName("section"), browser).setIndex(1);

        final BaseElement buttonSection = new ButtonImpl(By.xpath(".//button[@data-aid='startRegistration']"), section);
        final BaseElement buttonDiv = new ButtonImpl(By.xpath(".//button[@data-aid='startRegistration']"), div);

        browser.start();
        browser.open("https://app-digitalmortgage003.open.ru/");
        //browser.open("https://ipotekaonline.open.ru/");

        //browser.close();

        //log.info(Boolean.toString(buttonSection.isDisplayed()));
        //log.info(Boolean.toString(buttonDiv.isDisplayed()));

        log.info(buttonSection.getText());
        log.info(buttonDiv.getText());

        //log.info(Boolean.toString(buttonSection.isNotDisplayed()));
        //log.info(Boolean.toString(buttonDiv.isNotDisplayed()));

    }

    @Test
    public void testInputImpl() {
        final Input input = new InputImpl(By.id("lastNameId"), browser);

        browser.start();
        browser.open("https://app-digitalmortgage003.open.ru/registration");


        log.info(input.set("МАКС"));
        input.clear();
        Waiter.sleep(3000);
    }

    @After
    public void tearDown() throws Exception {
        browsers.close();
    }
}