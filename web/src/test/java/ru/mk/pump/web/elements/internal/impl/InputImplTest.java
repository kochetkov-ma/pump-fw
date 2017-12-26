package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class InputImplTest extends AbstractWebTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        createPages(browser);
        browser.open(regPage.getUrl());
    }

    @Test
    public void type() {
        log.info(regPage.getInputSurname().type("Кочетков"));
    }
}