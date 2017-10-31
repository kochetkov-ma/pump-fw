package ru.mk.pump.web.browsers;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.mk.pump.commons.utils.Resources;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;

public class AbstractBrowserTest {

    private Browser browser;

    @Before
    public void setUp() {
        final BrowserConfig browserConfig = new BrowserConfig(false, Size.of(true), BrowserType.CHROME);
        browserConfig.setWebDriverPath(Resources.findResource("chromedriver.exe").toString());

        browser = new Browsers().newBrowser(browserConfig);
    }

    @After
    public void tearDown() {
        browser.close();
    }

    @Test
    public void start() throws IOException {
        browser.start();
        browser.open("https://ipotekaonline-tst.open.ru/");
        browser.windows().newTab();
        browser.open("https://app-digitalmortgage003.open.ru/");
        browser.close();
    }

    @Test
    public void windows() {

    }

}