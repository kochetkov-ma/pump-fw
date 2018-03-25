package ru.mk.pump.web.elements.internal.impl;

import lombok.Getter;
import org.openqa.selenium.By;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.web.AbstractTestWithBrowser;
import ru.mk.pump.web.DMUrls;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.concrete.*;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.internal.BaseElement;

@SuppressWarnings("WeakerAccess")
abstract class AbstractWebTest extends AbstractTestWithBrowser {

    protected MainPage mainPage;

    protected RegPage regPage;

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

        private final Button flatDocs;

        private final Link flatDocsDownload;

        private MainPage(Browser browser) {
            /*parents*/
            parentDiv = new BaseElement(By.xpath(".//div[contains(@class,'b-calculator__rightblock')]"), browser)
                    .withParams(Parameters.of(ElementParams.FOCUS_CUSTOM_SCRIPT.withValue(SCROLL_REACT)));
            parentSection = new BaseElement(By.tagName("section"), browser).setIndex(1);

            /*childes*/
            childButtonSection = new BaseElement(By.xpath(".//button[@data-aid='startRegistration']"), parentDiv)
                    .withParams(Parameters.of(ElementParams.FOCUS_CUSTOM_SCRIPT.withValue(SCROLL_REACT)));
            childButtonDiv = new BaseElement(By.xpath(".//a[text()='Требования к заемщику']"), parentSection)
                    .withParams(Parameters.of(ElementParams.FOCUS_CUSTOM_SCRIPT.withValue(SCROLL_REACT)));

            /*failParent*/
            parentDivFail = new BaseElement(By.xpath(".//div[contains(@class,'b-calculator__rightblock1')]"), browser);
            /*failChild*/
            childButtonSectionFail = new BaseElement(By.xpath(".//button[@data-aid='startRegistration']"), parentDivFail);

            flatDocs = new ButtonImpl(By.xpath(".//button[text() = 'Перечень документов по приобретаемой квартире']"), browser);
            flatDocsDownload = new LinkImpl(By.xpath(".//a[text() = 'Ознакомьтесь с полным перечнем документов']"), browser);

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

        private final CheckBox checkbox;

        private RegPage(Browser browser) {
            inputSurname = new InputImpl(By.id("lastNameId"), browser);
            notExists = new BaseElement(By.xpath(".//div[@class='not_exists']"), browser);
            hiddenItems = new BaseElement(By.xpath(".//div[@class='items']"), browser);
            dropDownRegions = new DropDownImpl(By.id("regionAutocompleteId"), browser);
            ((BaseElement)dropDownRegions).withParams(Parameters.of(ElementParams.DROPDOWN_LOAD_BY.withStrictValue(new By[]{By.xpath("..//div[contains(@class,'loader')]")})));
            selectorProgram = new SelectorImpl(By.id("apartmentTypeId"), browser);
            inputDropDownRegions = new InputDropDownImpl(By.id("regionAutocompleteId"), browser);
            checkbox = new CheckBoxImpl(By.xpath("//input[@id='acceptCreditReportRequestId']/.."), browser);
        }
    }

    void createPages(Browser browser) {
        mainPage = new MainPage(browser);
        regPage = new RegPage(browser);
    }
}
