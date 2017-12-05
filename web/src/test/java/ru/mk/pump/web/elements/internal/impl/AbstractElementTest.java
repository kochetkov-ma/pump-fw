package ru.mk.pump.web.elements.internal.impl;

import lombok.Getter;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.ElementWaiter;

abstract class AbstractElementTest {

    private static final BrowserType BROWSER_TYPE = BrowserType.PHANTOMJS;

    Browsers browsers;

    Browser browser;

    MainPage mainPage;

    @Before
    public void setUp() {
        browsers = new Browsers();
        final BrowserConfig config = new BrowserConfig(false, Size.of(true), BROWSER_TYPE);
        if (BROWSER_TYPE == BrowserType.PHANTOMJS) {
            config.setWebDriverPath(ProjectResources.findResource("chromedriver.exe").toString());
        }
        browser = browsers.newBrowser(config);
    }

    @After
    public void tearDown() throws Exception {
        ElementWaiter.DEFAULT_TIMEOUT_S = 10;
        browsers.close();
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

    void createPages(Browser browser) {
        mainPage = new MainPage(browser);
    }

}
