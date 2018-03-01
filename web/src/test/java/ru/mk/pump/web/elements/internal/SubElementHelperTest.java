package ru.mk.pump.web.elements.internal;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.AbstractTestWithBrowser;
import ru.mk.pump.web.DMUrls;
import ru.mk.pump.web.elements.ElementConfig;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.api.concrete.TextArea;

import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class SubElementHelperTest extends AbstractTestWithBrowser {

    private TextArea parent;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ElementFactory elementFactory = new ElementFactory(new ElementImplDispatcher(), getBrowser());
        final ElementConfig configParent = ElementConfig.of("Тестовый элемент", "Для юнит теста")
                .withParameters(Parameters.of(Parameter.of("дополнительные xpath", By.class, "xpath")));
        final By byParent = By.tagName("section");
        parent = elementFactory.newElement(TextArea.class, byParent, configParent.withIndex(1));
        getBrowser().open(DMUrls.MAIN_PAGE_URL);
    }

    @Test
    void find() {
        parent.isDisplayed();
        final By byChild = By.xpath(".//button[@data-aid='startRegistration']");
        final Element res = parent.getSubElements(Button.class).find(byChild);
        log.info(res.toPrettyString());
        Assertions.assertThat(res).isNotNull();
    }

    @Test
    void findList() {
        parent.isDisplayed();
        final By byChild = By.xpath(".//button");
        final List<Button> res = parent.getSubElements(Button.class).findList(byChild);
        log.info(Strings.toPrettyString(res));
        Assertions.assertThat(res).hasSize(3);
    }

    @Test
    void findListXpathAdvanced() {
        parent.isDisplayed();
        final Predicate<WebElement> pred2 = (el) -> "startRegistration".equals(el.getAttribute("data-aid"));
        final Predicate<WebElement> pred1 = (el) -> "button".equals(el.getTagName());
        List<Button> res = parent.getSubElements(Button.class).findListXpathAdvanced(".//div", pred1, "/button");
        log.info(Strings.toPrettyString(res));
        Assertions.assertThat(res).hasSize(3);

        res = parent.getSubElements(Button.class).findListXpathAdvanced(".//div", pred2, "/button", "/button[text()='Подать заявку']");
        log.info(Strings.toPrettyString(res));
        Assertions.assertThat(res).hasSize(1);
    }

    @Test
    void findXpathAdvanced() {
        parent.isDisplayed();
        final Predicate<WebElement> pred = (el) -> "button".equals(el.getTagName());
        final Element res = parent.getSubElements(Button.class).findXpathAdvanced(".//div", pred, "/button", "/button[text()='Подать заявку']");
        log.info(res.toPrettyString());
        Assertions.assertThat(res).isNotNull();
    }
}