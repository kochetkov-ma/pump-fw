package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.internal.ElementWaiter;
import ru.mk.pump.web.exceptions.ActionExecutingException;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;
import ru.mk.pump.web.exceptions.ElementStateException;

@Slf4j
public class BaseElementTest extends AbstractElementTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
        createPages(browser);
    }

    @After
    public void tearDown(){
        browsers.close();
    }

    @Test
    public void testGetText() {
        browser.start();
        browser.open("https://app-digitalmortgage003.open.ru/");

        String res1 = mainPage.getParentDiv().getText();
        String res2 = mainPage.getParentDiv().getText();
        String res3 = mainPage.getChildButtonDiv().getText();
        String res4 = mainPage.getChildButtonSection().getText();

        log.info(res1);
        log.info(res2);
        log.info(res3);
        log.info(res4);

        Assertions.assertThat(res1).isNotBlank();
        Assertions.assertThat(res2).isNotBlank();
        Assertions.assertThat(res3).isNotBlank();
        Assertions.assertThat(res4).isNotBlank();
    }

    @Test
    public void testGetTextFail() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 1;
        createPages(browser);

        browser.start();
        browser.open(mainPage.getUrl());

        Assertions.assertThatThrownBy(() -> mainPage.getParentDivFail().getText())
            .isInstanceOf(ActionExecutingException.class)
            .hasMessageContaining("---action---")
            .hasCauseInstanceOf(ElementStateException.class)
            .has(new Condition<Throwable>(throwable -> throwable.getCause().getCause() instanceof ElementFinderNotFoundException, "cause"))
            .matches(throwable -> throwable.getCause().getCause().getCause() instanceof NoSuchElementException);

        Assertions.assertThatThrownBy(() -> mainPage.getChildButtonSectionFail().getText())
            .isInstanceOf(ActionExecutingException.class)
            .hasCauseInstanceOf(ElementStateException.class)
            .matches(throwable -> throwable.getCause().getCause() instanceof ElementFinderNotFoundException)
            .matches(throwable -> throwable.getCause().getCause().getCause() instanceof NoSuchElementException);


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
}