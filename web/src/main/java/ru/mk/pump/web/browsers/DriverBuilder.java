package ru.mk.pump.web.browsers;

import org.openqa.selenium.WebDriver;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;

public interface DriverBuilder {

    BrowserConfig getConfig();

    WebDriver createAndStartDriver();
}
