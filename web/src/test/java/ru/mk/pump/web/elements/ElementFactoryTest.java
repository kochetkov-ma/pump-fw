package ru.mk.pump.web.elements;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.api.ElementConfig;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.TextArea;
import ru.mk.pump.web.elements.internal.BaseElement;

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
        final Map<String, Parameter<?>> param = ImmutableMap.of("дополнительные xpath", Parameter.of(By::xpath, By.class).withValue("xpath"));
        final ElementConfig config = ElementConfig.of("Тестовый элемент", "Для юнит теста")
            .withParameters(param);
        final By by = By.tagName("div");
        log.info(Strings.toPrettyString(elementFactory.getInfo()));

        final Button button = elementFactory.newElement(Button.class, by, config);
        assertThat(button).isNotNull();
        assertThat(((BaseElement) button).getParams()).isEqualTo(param);
        assertThat(button.getName()).isEqualTo("Тестовый элемент");
        assertThat(button.getDescription()).isEqualTo("Для юнит теста");
        assertThat(button.getName()).isEqualTo("Тестовый элемент");

        final DropDown dropDown = elementFactory.newElement(DropDown.class, by, config);
        assertThat(dropDown).isNotNull();
        assertThat(((BaseElement) dropDown).getParams()).isEqualTo(param);
        assertThat(dropDown.getName()).isEqualTo("Тестовый элемент");
        assertThat(dropDown.getDescription()).isEqualTo("Для юнит теста");
        assertThat(dropDown.getName()).isEqualTo("Тестовый элемент");
        assertThat(dropDown).isNotNull();
    }

    @Test
    void newElementList() {
        final Map<String, Parameter<?>> param = ImmutableMap.of("дополнительные xpath", Parameter.of(By::xpath, By.class).withValue("xpath"),
            "еще параметр", Parameter.of("строка"));
        final ElementConfig configParent = ElementConfig.of("Тестовый элемент", "Для юнит теста")
            .withParameters(param);
        final ElementConfig configChild = ElementConfig.of("Тестовый элемент", "Для юнит теста")
            .withParameters(param);

        final By byParent = By.tagName("section");
        final By byChild = By.xpath(".//button[@data-aid='startRegistration']");

        final TextArea parent = elementFactory.newElement(TextArea.class, byParent, configParent.withIndex(1));
        assertThat(parent).isInstanceOf(TextArea.class);
        assertThat(((BaseElement) parent).getParams()).isEqualTo(param);
        assertThat(parent.getName()).isEqualTo("Тестовый элемент");
        assertThat(parent.getDescription()).isEqualTo("Для юнит теста");
        assertThat(parent.getName()).isEqualTo("Тестовый элемент");
        assertThat(parent.isList()).isTrue();
        assertThat(parent.getIndex()).isEqualTo(1);

        final Button button = elementFactory.newElement(Button.class, byChild, parent, configChild);
        assertThat(button).isInstanceOf(Button.class);
        assertThat(((BaseElement) button).getParams()).isEqualTo(param);
        assertThat(button.getName()).isEqualTo("Тестовый элемент");
        assertThat(button.getDescription()).isEqualTo("Для юнит теста");
        assertThat(button.getName()).isEqualTo("Тестовый элемент");
        assertThat(button.isList()).isFalse();
        assertThat(button.getIndex()).isEqualTo(-1);
    }
}