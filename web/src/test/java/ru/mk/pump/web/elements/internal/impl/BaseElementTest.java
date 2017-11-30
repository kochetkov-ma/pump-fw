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
public class BaseElementTest extends AbstractElementTest{

    @Test
    public void testGetText() {
        browser.start();
        browser.open(mainPage.getUrl());

        log.info(mainPage.getParentDiv().getText());
        log.info(mainPage.getParentDiv().getText());

        log.info(mainPage.getChildButtonDiv().getText());
        log.info(mainPage.getChildButtonSection().getText());
    }

    @Test
    public void testClick() {
        browser.start();
        browser.open(mainPage.getUrl());

        log.info(mainPage.getChildButtonDiv().getText());
        log.info(mainPage.getChildButtonSection().getText());
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