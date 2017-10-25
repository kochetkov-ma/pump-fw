package ru.mk.pump.web.browsers;

import org.openqa.selenium.WebDriver;

public interface DriverBuilder {

    BrowserConfig getConfig();

    DriverBuilder setConfig(BrowserConfig browserConfig);

    WebDriver createAndStartDriver();
}
