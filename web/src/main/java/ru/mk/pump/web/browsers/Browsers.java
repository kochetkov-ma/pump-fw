package ru.mk.pump.web.browsers;

import java.util.UUID;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Browsers {

    private static DriverBuilder getBuilder(BrowserConfig browserConfig) {
        return new DriverBuilder() {
            @Override
            public BrowserConfig getConfig() {
                return browserConfig;
            }

            @Override
            public DriverBuilder setConfig(BrowserConfig browserConfig) {
                return this;
            }

            @Override
            public WebDriver createAndStartDriver() {
                return new ChromeDriver();
            }
        };
    }

    public Browser newBrowser(BrowserConfig browserConfig) {
        return new AbstractBrowser(Browsers.getBuilder(browserConfig), browserConfig, UUID.randomUUID()) {
        };
    }
}
