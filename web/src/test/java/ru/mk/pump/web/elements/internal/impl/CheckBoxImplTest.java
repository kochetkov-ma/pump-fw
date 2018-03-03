package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.DMUrls;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.enums.CheckBoxState;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CheckBoxImplTest extends AbstractWebTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        createPages(getBrowser());
    }

    @Test
    void set() {
        getBrowser().open(DMUrls.REG_PAGE_URL);
        regPage.getCheckbox().set(ElementParams.EDITABLE_SET_CHECKBOX.withValue(CheckBoxState.UNCHECKED));
        assertThat(regPage.getCheckbox().getState()).isEqualTo(CheckBoxState.UNCHECKED);
        regPage.getCheckbox().set(ElementParams.EDITABLE_SET_CHECKBOX.withValue(CheckBoxState.CHECKED));
        assertThat(regPage.getCheckbox().getState()).isEqualTo(CheckBoxState.CHECKED);
        regPage.getCheckbox().set(ElementParams.EDITABLE_SET_CHECKBOX.withValue(CheckBoxState.UNCHECKED));
        assertThat(regPage.getCheckbox().getState()).isEqualTo(CheckBoxState.UNCHECKED);
    }

    @Test
    void getSetState() {
        getBrowser().open(DMUrls.REG_PAGE_URL);
        regPage.getCheckbox().setState(CheckBoxState.UNCHECKED);
        assertThat(regPage.getCheckbox().getState()).isEqualTo(CheckBoxState.UNCHECKED);
        regPage.getCheckbox().setState(CheckBoxState.CHECKED);
        assertThat(regPage.getCheckbox().getState()).isEqualTo(CheckBoxState.CHECKED);
        regPage.getCheckbox().setState(CheckBoxState.UNCHECKED);
        assertThat(regPage.getCheckbox().getState()).isEqualTo(CheckBoxState.UNCHECKED);
    }
}