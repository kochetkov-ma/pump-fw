package ru.mk.pump.web.browsers;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;

public class AbstractBrowserTest {

    private Browser browser;

    @Before
    public void setUp() {
        final BrowserConfig browserConfig = new BrowserConfig(false, Size.of(true), BrowserType.CHROME);
        browserConfig.setWebDriverPath(ProjectResources.findResource("chromedriver.exe").toString());

        browser = new Browsers().newBrowser(browserConfig);
    }

    @After
    public void tearDown() {
        browser.close();
    }

    @Test
    public void start() throws IOException {
        browser.start();
        browser.open("https://ya.ru");
        browser.windows().newTab();
        browser.open("https://google.ru");
        browser.close();
    }

    @Test
    @Ignore
    public void windows() {

    }

}