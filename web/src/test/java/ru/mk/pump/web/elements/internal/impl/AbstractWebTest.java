package ru.mk.pump.web.elements.internal.impl;

import lombok.Getter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import ru.mk.pump.commons.utils.EnvVariables;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.api.concrete.Selector;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.ElementWaiter;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractWebTest {

    private static final BrowserType BROWSER_TYPE = BrowserType.valueOf(EnvVariables.get("BROWSER", "PHANTOMJS"));
    //private static final BrowserType BROWSER_TYPE = BrowserType.CHROME;

    public static Browsers browsers;

    public Browser browser;

    public MainPage mainPage;

    public RegPage regPage;

    public static BrowserConfig config;

    @AfterAll
    public static void afterAll() {
        browsers.close();
    }

    @BeforeAll
    public static void beforeAll() {
        config = new BrowserConfig(false, Size.of(true), BROWSER_TYPE);
        if (BROWSER_TYPE != BrowserType.PHANTOMJS) {
            config.setWebDriverPath(ProjectResources.findResource("chromedriver.exe").toString());
        }
        browsers = new Browsers();
    }

    @BeforeEach
    public void setUp() {
        if (!browsers.has()) {
            browser = createBrowser();
            browser.start();
        } else {
            browser = browsers.get();
        }
    }

    @AfterEach
    public void tearDown() {

        ElementWaiter.DEFAULT_TIMEOUT_S = 10;
    }

    void createPages(Browser browser) {
        mainPage = new MainPage(browser);
        regPage = new RegPage(browser);
    }

    @SuppressWarnings("WeakerAccess")
    @Getter
    class MainPage {

        private final BaseElement childButtonSectionFail;

        private final BaseElement parentDivFail;

        private final String url = "https://app-digitalmortgage003.open.ru/";

        private final BaseElement parentDiv;

        private final BaseElement parentSection;

        private final BaseElement childButtonSection;

        private final BaseElement childButtonDiv;

        private MainPage(Browser browser) {
            /*parents*/
            parentDiv = new BaseElement(By.xpath(".//div[contains(@class,'b-calculator__rightblock')]"), browser);
            parentSection = new BaseElement(By.tagName("section"), browser).setIndex(1);

            /*childes*/
            childButtonSection = new BaseElement(By.xpath(".//button[@data-aid='startRegistration']"), parentDiv);
            childButtonDiv = new BaseElement(By.xpath(".//a[text()='Требования к заемщику']"), parentSection);

            /*failParent*/
            parentDivFail = new BaseElement(By.xpath(".//div[contains(@class,'b-calculator__rightblock1')]"), browser);
            /*failChild*/
            childButtonSectionFail = new BaseElement(By.xpath(".//button[@data-aid='startRegistration']"), parentDivFail);

        }
    }

    @SuppressWarnings("WeakerAccess")
    @Getter
    class RegPage {

        private final String url = "https://app-digitalmortgage003.open.ru/registration";

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

    protected Browser createBrowser() {
        return browsers.newBrowser(config);
    }

}
