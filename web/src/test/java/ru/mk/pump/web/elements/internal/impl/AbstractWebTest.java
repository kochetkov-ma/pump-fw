package ru.mk.pump.web.elements.internal.impl;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.AbstractTestWithBrowser;
import ru.mk.pump.web.DMUrls;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.api.concrete.Selector;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.internal.BaseElement;

@SuppressWarnings("WeakerAccess")
abstract class AbstractWebTest extends AbstractTestWithBrowser {

    protected MainPage mainPage;

    protected RegPage regPage;

    void createPages(Browser browser) {
        mainPage = new MainPage(browser);
        regPage = new RegPage(browser);
    }

    @SuppressWarnings("WeakerAccess")
    @Getter
    class MainPage {

        private static final String SCROLL_REACT = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
            + "var elementTop = arguments[0].getBoundingClientRect().top;"
            + "document.getElementById('app').scrollBy(0, elementTop-(viewPortHeight/2));";

        private final BaseElement childButtonSectionFail;

        private final BaseElement parentDivFail;

        private final String url = DMUrls.MAIN_PAGE_URL;

        private final BaseElement parentDiv;

        private final BaseElement parentSection;

        private final BaseElement childButtonSection;

        private final BaseElement childButtonDiv;

        private MainPage(Browser browser) {
            /*parents*/
            parentDiv = new BaseElement(By.xpath(".//div[contains(@class,'b-calculator__rightblock')]"), browser)
                .withParams(ImmutableMap.of(ElementParams.FOCUS_CUSTOM_SCRIPT, Parameter.of(SCROLL_REACT)));
            parentSection = new BaseElement(By.tagName("section"), browser).setIndex(1);

            /*childes*/
            childButtonSection = new BaseElement(By.xpath(".//button[@data-aid='startRegistration']"), parentDiv)
                .withParams(ImmutableMap.of(ElementParams.FOCUS_CUSTOM_SCRIPT, Parameter.of(SCROLL_REACT)));
            childButtonDiv = new BaseElement(By.xpath(".//a[text()='Требования к заемщику']"), parentSection)
                .withParams(ImmutableMap.of(ElementParams.FOCUS_CUSTOM_SCRIPT, Parameter.of(SCROLL_REACT)));

            /*failParent*/
            parentDivFail = new BaseElement(By.xpath(".//div[contains(@class,'b-calculator__rightblock1')]"), browser);
            /*failChild*/
            childButtonSectionFail = new BaseElement(By.xpath(".//button[@data-aid='startRegistration']"), parentDivFail);

        }
    }

    @SuppressWarnings("WeakerAccess")
    @Getter
    class RegPage {

        private final String url = DMUrls.REG_PAGE_URL;

        private final BaseElement hiddenItems;

        private final BaseElement notExists;

        private final InputDropDown inputDropDownRegions;

        private final DropDown dropDownRegions;

        private final Selector selectorProgram;

        private final Input inputSurname;

        private RegPage(Browser browser) {
            inputSurname = new InputImpl(By.id("lastNameId"), browser);
            notExists = new BaseElement(By.xpath(".//div[@class='not_exists']"), browser);
            hiddenItems = new BaseElement(By.xpath(".//div[@class='items']"), browser);
            dropDownRegions = new DropDownImpl(By.id("regionAutocompleteId"), browser);
            selectorProgram = new SelectorImpl(By.id("apartmentTypeId"), browser);
            inputDropDownRegions = new InputDropDownImpl(By.id("regionAutocompleteId"), browser);
        }
    }
}
