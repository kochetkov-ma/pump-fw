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

abstract class AbstractElementTest {

    protected Browsers browsers;

    protected Browser browser;

    protected MainPage mainPage;

    @Before
    public void setUp() {
        browsers = new Browsers();
        final BrowserConfig config = new BrowserConfig(false, Size.of(true), BrowserType.CHROME);
        config.setWebDriverPath(ProjectResources.findResource("chromedriver.exe").toString());
        browser = browsers.newBrowser(config);

        createPages(browser);
    }

    @After
    public void tearDown() throws Exception {
        browsers.close();
    }

    @Getter
    class MainPage {

        private String url = "https://app-digitalmortgage003.open.ru/";

        private BaseElement parentDiv;

        private BaseElement parentSection;

        private BaseElement childButtonSection;

        private BaseElement childButtonDiv;

        private MainPage(Browser browser) {
            /*parents*/
            parentDiv = new BaseElement(By.xpath(".//div[contains(@class,'b-calculator__rightblock')]"), browser);
            parentSection = new BaseElement(By.tagName("section"), browser).setIndex(1);

            /*childes*/
            childButtonSection = new BaseElement(By.xpath(".//button[@data-aid='startRegistration']"), parentDiv);
            childButtonDiv = new BaseElement(By.xpath(".//button[@text()='Требования к заемщику']"), parentSection);
        }
    }

    private void createPages(Browser browser) {
        mainPage = new MainPage(browser);
    }

}
