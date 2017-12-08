package ru.mk.pump.web.browsers;

import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;

public class AbstractBrowserTest {

    private Browser browser;

    @BeforeEach
    public void setUp() {
        final BrowserConfig browserConfig = new BrowserConfig(false, Size.of(true), BrowserType.CHROME);
        browserConfig.setWebDriverPath(ProjectResources.findResource("chromedriver.exe").toString());

        browser = new Browsers().newBrowser(browserConfig);
    }

    @AfterEach
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
    @Disabled
    public void windows() {

    }

}