package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.utils.Waiter;

@Slf4j
class SelectorImplTest extends AbstractElementTest{

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        createPages(browser);
    }

    @Test
    void initFromParams() {

    }

    @Test
    void getExtraBy() {
    }

    @Test
    void selectString() {
        browser.open(regPage.getUrl());
        regPage.getSelectorProgram().select("Вторич_");
        log.info(regPage.getSelectorProgram().getSelected().getText());
    }

    @Test
    void selectInt() {
        browser.open(regPage.getUrl());
        regPage.getSelectorProgram().select(1);
        log.info(regPage.getSelectorProgram().getSelected().getText());
    }

    @Test
    void getSelected() {
        browser.open(regPage.getUrl());
        log.info(regPage.getSelectorProgram().getSelected().getText());

    }

    @Test
    void getItems() {
    }

    @Test
    void set() {
    }
}