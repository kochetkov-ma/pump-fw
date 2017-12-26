package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.elements.internal.ElementWaiter;

@Slf4j
class DropDownImplTest extends AbstractWebTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        createPages(browser);
        browser.open(regPage.getUrl());
    }

    @Test
    void selectString() {
        regPage.getDropDownRegions().select("Ямало-Ненецкий автономный");
    }

    @Test
    void selectIndex() {
        regPage.getDropDownRegions().select(3);
    }

    @Test
    void items() {
        Assertions.assertThat(regPage.getDropDownRegions().getItems()).hasSize(20);
    }

    @Test
    void isExpand() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 3;
        createPages(browser);
        Assertions.assertThat(regPage.getDropDownRegions().isExpand()).isFalse();
        /*тэг select всегда развернут и его элементы видимы и по ним можно нормально кикать, хотя в браузере не рендерится ...*/
    }

    @Test
    void isNotExpand() {
        createPages(browser);

        Assertions.assertThat(regPage.getDropDownRegions().isNotExpand()).isTrue();
    }


    @Test
    void expand() {
        regPage.getDropDownRegions().expand();
        Assertions.assertThat(regPage.getDropDownRegions().isExpand()).isTrue();
    }
}