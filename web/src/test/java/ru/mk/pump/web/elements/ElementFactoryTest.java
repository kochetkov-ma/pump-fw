package ru.mk.pump.web.elements;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.TextArea;
import ru.mk.pump.web.elements.internal.BaseElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@Slf4j
class ElementFactoryTest {

    private ElementFactory elementFactory;

    private Browser browser = mock(Browser.class);

    @BeforeEach
    void setUp() {
        elementFactory = new ElementFactory(new ElementImplDispatcher(), browser);
    }

    @Test
    void withMainRequirements() {
    }

    @Test
    void addActionListener() {
    }

    @Test
    void withActionListener() {
    }

    @Test
    void newElement() {
        final Parameters param = Parameters.of(Parameter.of("дополнительные xpath", By.class, "xpath"));
        final ElementConfig config = ElementConfig.of("Тестовый элемент", "Для юнит теста")
                .withParameters(param);
        final By by = By.tagName("div");
        log.info(Str.toPrettyString(elementFactory.getInfo()));

        final Button button = elementFactory.newElement(Button.class, by, config);
        assertThat(button).isNotNull();
        assertThat(((BaseElement) button).getParams()).isEqualTo(param);
        assertThat(button.info().getName()).isEqualTo("Тестовый элемент");
        assertThat(button.info().getDescription()).isEqualTo("Для юнит теста");
        assertThat(button.info().getName()).isEqualTo("Тестовый элемент");

        final DropDown dropDown = elementFactory.newElement(DropDown.class, by, config);
        assertThat(dropDown).isNotNull();
        assertThat(((BaseElement) dropDown).getParams()).isEqualTo(param);
        assertThat(dropDown.info().getName()).isEqualTo("Тестовый элемент");
        assertThat(dropDown.info().getDescription()).isEqualTo("Для юнит теста");
        assertThat(dropDown.info().getName()).isEqualTo("Тестовый элемент");
        assertThat(dropDown).isNotNull();
    }

    @Test
    void newElementList() {
        final Parameters param = Parameters.of(Parameter.of("дополнительные xpath", By.class, "xpath"),
                Parameter.of("еще параметр", "строка"));
        final ElementConfig configParent = ElementConfig.of("Тестовый элемент", "Для юнит теста")
                .withParameters(param);
        final ElementConfig configChild = ElementConfig.of("Тестовый элемент", "Для юнит теста")
                .withParameters(param);

        final By byParent = By.tagName("section");
        final By byChild = By.xpath(".//button[@data-aid='startRegistration']");

        final TextArea parent = elementFactory.newElement(TextArea.class, byParent, configParent.withIndex(1));
        assertThat(parent).isInstanceOf(TextArea.class);
        assertThat(((BaseElement) parent).getParams()).isEqualTo(param);
        assertThat(parent.info().getName()).isEqualTo("Тестовый элемент");
        assertThat(parent.info().getDescription()).isEqualTo("Для юнит теста");
        assertThat(parent.info().getName()).isEqualTo("Тестовый элемент");
        assertThat(parent.advanced().isList()).isTrue();
        assertThat(parent.advanced().getIndex()).isEqualTo(1);

        final Button button = elementFactory.newElement(Button.class, byChild, parent, configChild);
        assertThat(button).isInstanceOf(Button.class);
        assertThat(((BaseElement) button).getParams()).isEqualTo(param);
        assertThat(button.info().getName()).isEqualTo("Тестовый элемент");
        assertThat(button.info().getDescription()).isEqualTo("Для юнит теста");
        assertThat(button.info().getName()).isEqualTo("Тестовый элемент");
        assertThat(button.advanced().isList()).isFalse();
        assertThat(button.advanced().getIndex()).isEqualTo(-1);
    }
}