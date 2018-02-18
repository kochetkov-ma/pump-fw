package ru.mk.pump.cucumber.steps.web;

import cucumber.api.java.en.Given;
import ru.mk.pump.cucumber.CucumberPumpCore;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.configuration.ConfigurationHolder;

public class BrowserSteps {
    @Given("^Browser - new '(.+)'$")
    public void newBrowser(BrowserType browserType){


    }
}
