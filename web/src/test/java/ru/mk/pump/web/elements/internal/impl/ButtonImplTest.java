package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;

@Slf4j
public class ButtonImplTest {

    private Browsers browsers;

    private BrowserConfig config;

    private Browser browser;

    @Before
    public void setUp(){
        browsers = new Browsers();
        config = new BrowserConfig(false, Size.of(true), BrowserType.CHROME);
        browser = browsers.newBrowser(config);
    }


    @Test
    public void test() {
        final BaseElement section = new ButtonImpl(By.tagName("section"), browser).setIndex(1);

        final BaseElement button = new ButtonImpl(By.xpath(".//button[@data-aid='startRegistration']"), section);

        browser.start();
        browser.open("https://app-digitalmortgage003.open.ru/");

        log.info(button.getText());

    }

    @After
    public void tearDown() throws Exception {
        browsers.close();
    }
}