package ru.mk.pump.web.browsers;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;

public class AbstractBrowserTest {

    private Browser browser;

    @Before
    public void setUp() {
        browser = new Browsers().newBrowser(new BrowserConfig(BrowserType.CHROME));
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
        browser.windows().getPrev().get().activate();
        browser.getDriver().findElement(By.id("login")).click();
        browser.windows().getPrev().get().close();
        browser.windows().getActive().get().close();
        browser.close();
    }

    @Test
    public void windows() {
    }
}