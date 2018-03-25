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
        createPages(getBrowser());
        getBrowser().open(regPage.getUrl());
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
        getBrowser().open(regPage.getUrl());
        Assertions.assertThat(regPage.getDropDownRegions().getItems()).hasSize(20);
    }

    @Test
    void isExpand() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 5;
        createPages(getBrowser());
        Assertions.assertThat(regPage.getDropDownRegions().isExpand()).isFalse();
        /*тэг select всегда развернут и его элементы видимы и по ним можно нормально кикать, хотя в браузере не рендерится ...*/
    }

    @Test
    void isNotExpand() {
        createPages(getBrowser());
        Assertions.assertThat(regPage.getDropDownRegions().isNotExpand()).isTrue();
    }


    @Test
    void expand() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 1;
        regPage.getDropDownRegions().expand();
        Assertions.assertThat(regPage.getDropDownRegions().isExpand()).isTrue();
    }
}