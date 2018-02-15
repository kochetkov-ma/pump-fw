package ru.mk.pump.web.elements.internal.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.DMUrls;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.ElementWaiter;
import ru.mk.pump.web.exceptions.ActionExecutingException;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;
import ru.mk.pump.web.exceptions.ElementStateException;

@Slf4j
public class BaseElementTest extends AbstractWebTest {


    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        createPages(getBrowser());
    }

    @Test
    void testGetTextSilent() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 1;
        createPages(getBrowser());

        getBrowser().open(regPage.getUrl());

        Assertions.assertThatThrownBy(() -> regPage.getHiddenItems().getText())
            .isInstanceOf(ActionExecutingException.class);
        final String res = regPage.getHiddenItems().getTextHidden();
        log.info(res);
        Assertions.assertThat(res)
            .isNotEmpty();
    }

    @Test
    void testGetText() {
        getBrowser().open(DMUrls.MAIN_PAGE_URL);

        String res1 = mainPage.getParentDiv().getText();
        String res2 = mainPage.getParentDiv().getText();
        String res3 = mainPage.getChildButtonDiv().getText();
        String res4 = mainPage.getChildButtonSection().getText();

        log.info(res1);
        log.info(res2);
        log.info(res3);
        log.info(res4);

        Assertions.assertThat(res1).isNotBlank();
        Assertions.assertThat(res2).isNotBlank();
        Assertions.assertThat(res3).isNotBlank();
        Assertions.assertThat(res4).isNotBlank();
    }

    @Test
    void testGetTextFail() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 1;
        createPages(getBrowser());

        getBrowser().open(mainPage.getUrl());

        Assertions.assertThatThrownBy(() -> mainPage.getParentDivFail().getText())
            .isInstanceOf(ActionExecutingException.class)
            .hasMessageContaining("---action---")
            .hasCauseInstanceOf(ElementStateException.class)
            .has(new Condition<Throwable>(throwable -> throwable.getCause().getCause() instanceof ElementFinderNotFoundException, "cause"))
            .matches(throwable -> throwable.getCause().getCause().getCause() instanceof NoSuchElementException);

        Assertions.assertThatThrownBy(() -> mainPage.getChildButtonSectionFail().getText())
            .isInstanceOf(ActionExecutingException.class)
            .hasCauseInstanceOf(ElementStateException.class)
            .matches(throwable -> throwable.getCause().getCause() instanceof ElementFinderNotFoundException)
            .matches(throwable -> throwable.getCause().getCause().getCause() instanceof NoSuchElementException);
    }

    @Test
    void testClick() {
        getBrowser().open(mainPage.getUrl());
        mainPage.getChildButtonSection().click();
    }

    @Test
    void testClickFail() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 1;
        createPages(getBrowser());

        getBrowser().open(regPage.getUrl());

        Assertions.assertThat(regPage.getHiddenItems().isNotEnabled()).isFalse();

        getBrowser().open(mainPage.getUrl());

        Assertions.assertThatThrownBy(() -> mainPage.getParentDivFail().click())
            .isInstanceOf(ActionExecutingException.class)
            .hasMessageContaining("Executing action 'Click' error")
            .hasMessageContaining("type : Click")
            .hasCauseInstanceOf(ElementStateException.class)
            .matches(throwable -> throwable.getCause().getCause() instanceof ElementFinderNotFoundException)
            .matches(throwable -> throwable.getCause().getCause().getCause() instanceof NoSuchElementException);
    }

    @Test
    void clear() {
        getBrowser().open(regPage.getUrl());

        log.info(regPage.getInputSurname().type("МАКС"));
        regPage.getInputSurname().clear();
        Assertions.assertThat(regPage.getInputSurname().getTextHidden()).isEmpty();
    }

    @Test
    void isAllState() {
        getBrowser().open(regPage.getUrl());

        Assertions.assertThat(regPage.getHiddenItems().isExists()).isTrue();
        Assertions.assertThat(regPage.getInputSurname().isDisplayed()).isTrue();
        Assertions.assertThat(regPage.getInputSurname().isEnabled()).isTrue();
    }

    @Test
    void isNotAllState() {
        getBrowser().open(regPage.getUrl());

        Assertions.assertThat(regPage.getHiddenItems().isNotDisplayed()).isTrue();
        Assertions.assertThat(regPage.getNotExists().isNotEnabled()).isTrue();
        Assertions.assertThat(regPage.getNotExists().isNotExists()).isTrue();

        Assertions.assertThat(regPage.getHiddenItems().isExists()).isTrue();

        Assertions.assertThat(regPage.getHiddenItems().isNotDisplayed()).isTrue();
        Assertions.assertThat(regPage.getNotExists().isNotEnabled()).isTrue();
        Assertions.assertThat(regPage.getNotExists().isNotExists()).isTrue();
    }

    @Test
    void isNotAllStateFail() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 3;
        createPages(getBrowser());

        getBrowser().open(regPage.getUrl());

        Assertions.assertThat(regPage.getHiddenItems().isNotEnabled()).isFalse();
        Assertions.assertThat(regPage.getInputSurname().isNotEnabled()).isFalse();
        Assertions.assertThat(regPage.getInputSurname().isNotExists()).isFalse();
    }

    @Test
    void isAllStateFail() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 3;
        createPages(getBrowser());

        getBrowser().open(regPage.getUrl());

        Assertions.assertThat(regPage.getHiddenItems().isDisplayed()).isFalse();
        Assertions.assertThat(regPage.getNotExists().isEnabled()).isFalse();
        Assertions.assertThat(regPage.getNotExists().isExists()).isFalse();

    }

    @Test
    void getSubElements() {
        getBrowser().open(regPage.getUrl());

        List<Element> elementList = regPage.getDropDownRegions().getSubElements(Element.class).findListXpathAdvanced("//div[@class='item']",
            (el) -> el.getAttribute("class").equals("item"));
        log.info(Strings.toPrettyString(elementList));
        Assertions.assertThat(elementList).hasSize(20);

        elementList = regPage.getDropDownRegions().getSubElements(Element.class).findListXpathAdvanced(".//div[@class='items']",
            (el) -> el.getAttribute("class").equals("item"),
            ".//div[@class='item']");
        log.info(Strings.toPrettyString(elementList));
        Assertions.assertThat(elementList).hasSize(20);

        elementList = regPage.getDropDownRegions().getSubElements(Element.class).findListXpathAdvanced(null,
            (el) -> el.getAttribute("class").equals("item"),
            ".//div[@class='item']");
        log.info(Strings.toPrettyString(elementList));
        Assertions.assertThat(elementList).hasSize(20);

        elementList = regPage.getDropDownRegions().getSubElements(Element.class).findListXpathAdvanced("",
            (el) -> el.getAttribute("class").equals("item"),
            ".//div[@class='item']");
        log.info(Strings.toPrettyString(elementList));
        Assertions.assertThat(elementList).hasSize(20);

        elementList = regPage.getDropDownRegions().getSubElements(Element.class).findList(By.xpath("//div[@class='items']"), By.className("item"));
        log.info(Strings.toPrettyString(elementList));
        Assertions.assertThat(elementList).hasSize(1);

        elementList = regPage.getDropDownRegions().getSubElements(Element.class)
            .findList(els -> els.size() > 1, By.xpath("//div[@class='items']"), By.className("item"));
        log.info(Strings.toPrettyString(elementList));
        Assertions.assertThat(elementList).hasSize(20);

        Element element = regPage.getDropDownRegions().getSubElements(Element.class)
            .find(By.xpath("//div[@class='items']"), By.className("item"));
        log.info(Strings.toString(element));
        Assertions.assertThat(element).isNotNull();

        element = regPage.getDropDownRegions().getSubElements(Element.class)
            .findXpathAdvanced(".//div[@class='items']",
                (el) -> el.getAttribute("class").equals("item"),
                ".//div[@class='item']");
        log.info(Strings.toString(element));
        Assertions.assertThat(element).isNotNull();
    }
}