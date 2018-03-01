package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.enums.SelectedStrategy;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.ElementWaiter;
import ru.mk.pump.web.exceptions.ActionExecutingException;
import ru.mk.pump.web.exceptions.SubElementsNotFoundException;

@Slf4j
class SelectorImplTest extends AbstractWebTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        createPages(getBrowser());
        getBrowser().open(regPage.getUrl());
    }

    @Test
    void set() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 3;
        createPages(getBrowser());

        Assertions.assertThatThrownBy(() -> regPage.getSelectorProgram().set(Parameter.of("fail", "Вторич")))
                .isInstanceOf(IllegalArgumentException.class);

        regPage.getSelectorProgram().set(ElementParams.EDITABLE_SET_STRING.withValue("Вторич"));
        Assertions.assertThat(regPage.getSelectorProgram().getSelected().getText()).isEqualTo("Вторичное жилье");

        regPage.getSelectorProgram().set(ElementParams.EDITABLE_SET_NUMBER.withValue(0));
        Assertions.assertThat(regPage.getSelectorProgram().getSelected().getText()).isEqualTo("Новостройка");
    }

    @Test
    void selectString() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 3;
        createPages(getBrowser());
        ((BaseElement) regPage.getSelectorProgram())
                .withParams(Parameters.of(ElementParams.SELECTED_STRATEGY.withValue(SelectedStrategy.EQUALS),
                        ElementParams.SELECTED_MARK.withValue("selected")));

        Assertions.assertThatThrownBy(() -> regPage.getSelectorProgram().select("Вторич"))
                .isInstanceOf(ActionExecutingException.class)
                .hasMessageContaining("Executing action 'Select item by text Вторич' error")
                .hasCauseInstanceOf(SubElementsNotFoundException.class);

        ((BaseElement) regPage.getSelectorProgram())
                .withParams(
                        Parameters.of(ElementParams.SELECTED_STRATEGY.withValue(SelectedStrategy.CONTAINS),
                                ElementParams.SELECTED_MARK.withValue("selected")));

        regPage.getSelectorProgram().select("Вторич");
        Assertions.assertThat(regPage.getSelectorProgram().getSelected().getText()).isEqualTo("Вторичное жилье");
    }

    @Test
    void selectInt() {
        regPage.getSelectorProgram().select(1);
        Assertions.assertThat(regPage.getSelectorProgram().getSelected().getText()).isEqualTo("Вторичное жилье");
    }

    @Test
    void getSelected() {
        Assertions.assertThat(regPage.getSelectorProgram().getSelected().getText()).isEqualTo("Новостройка");
    }

    @Test
    void getItems() {
        log.info(Strings.toPrettyString(regPage.getSelectorProgram().getItems()));
        Assertions.assertThat(regPage.getSelectorProgram().getItems()).hasSize(2);
    }
}