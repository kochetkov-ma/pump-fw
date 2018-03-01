package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.constants.ElementParams;

@Slf4j
class InputDropDownImplTest extends AbstractWebTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        createPages(getBrowser());
        getBrowser().open(regPage.getUrl());
    }

    @Test
    void type() {
        Assertions.assertThat(regPage.getInputDropDownRegions().type("Москва")).isEqualTo("Москва");
    }

    @Test
    void set() {
        regPage.getInputDropDownRegions().set(ElementParams.EDITABLE_SET_STRING.withValue("Мос"));
        regPage.getInputDropDownRegions()
                .set(ElementParams.EDITABLE_SET_STRING.withValue("Мос"), ElementParams.INPUT_DROPDOWN_SET_STRING.withValue("Москва"));
    }

    @Test
    void getText() {
        String res = regPage.getInputDropDownRegions().getText();
        log.info(res);
        regPage.getInputDropDownRegions().set(ElementParams.EDITABLE_SET_STRING.withValue("Москва"));
        res = regPage.getInputDropDownRegions().getText();
        log.info(res);
    }

    @Test
    void getTextHidden() {
        String res = regPage.getInputDropDownRegions().getTextHidden();
        log.info(res);
        regPage.getInputDropDownRegions().set(ElementParams.EDITABLE_SET_STRING.withValue("Москва"));
        res = regPage.getInputDropDownRegions().getTextHidden();
        log.info(res);
    }

    @Test
    void getDropDown() {
    }

    @Test
    void isExpand() {
    }

    @Test
    void isNotExpand() {
    }

    @Test
    void expand() {
    }

    @Test
    void select() {
    }

    @Test
    void select1() {
    }

    @Test
    void getSelected() {
    }

    @Test
    void getItems() {
    }

    @Test
    void typeAndSelect() {
    }

    @Test
    void typeAndSelect1() {
    }
}