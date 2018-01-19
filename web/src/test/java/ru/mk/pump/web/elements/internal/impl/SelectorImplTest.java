package ru.mk.pump.web.elements.internal.impl;

import static ru.mk.pump.web.constants.ElementParams.SELECTED_MARK;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.activity.Parameter;
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

        Assertions.assertThatThrownBy(() -> regPage.getSelectorProgram().set(Collections.singletonMap("fail", Parameter.of("Вторич"))))
            .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> regPage.getSelectorProgram().set(Collections.singletonMap(ElementParams.EDITABLE_SET, Parameter.of(Long.class, 0L))))
            .isInstanceOf(IllegalArgumentException.class);

        regPage.getSelectorProgram().set(Collections.singletonMap(ElementParams.EDITABLE_SET, Parameter.of("Вторич")));
        Assertions.assertThat(regPage.getSelectorProgram().getSelected().getText()).isEqualTo("Вторичное жилье");

        regPage.getSelectorProgram().set(Collections.singletonMap(ElementParams.EDITABLE_SET, Parameter.of(Integer.class, 0)));
        Assertions.assertThat(regPage.getSelectorProgram().getSelected().getText()).isEqualTo("Новостройка");
    }

    @Test
    void selectString() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 3;
        createPages(getBrowser());
        ((BaseElement) regPage.getSelectorProgram())
            .withParams(
                ImmutableMap.<String, Parameter<?>>builder().putAll(ElementParams.enumAsParam(SelectedStrategy.EQUALS)).put(SELECTED_MARK, Parameter.of("selected"))
                    .build());

        Assertions.assertThatThrownBy(()->regPage.getSelectorProgram().select("Вторич"))
            .isInstanceOf(ActionExecutingException.class)
            .hasMessageContaining("Executing action 'Select item by text Вторич' error")
            .hasCauseInstanceOf(SubElementsNotFoundException.class);

        ((BaseElement) regPage.getSelectorProgram())
            .withParams(
                ImmutableMap.<String, Parameter<?>>builder().putAll(ElementParams.enumAsParam(SelectedStrategy.CONTAINS)).put(SELECTED_MARK, Parameter.of("selected"))
                    .build());

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