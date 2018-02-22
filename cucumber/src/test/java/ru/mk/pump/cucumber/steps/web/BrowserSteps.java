package ru.mk.pump.cucumber.steps.web;

import cucumber.api.java.en.Given;
import ru.mk.pump.cucumber.CucumberPumpCore;
import ru.mk.pump.cucumber.steps.AbstractSteps;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.configuration.ConfigurationHolder;

public class BrowserSteps extends AbstractSteps {

    @Given("^Browser - new '(.+)'$")
    public void newBrowser(BrowserType browserType) {
        BrowserConfig config = ConfigurationHolder.get().getBrowserConfig();
        config.setType(browserType);
        core().getBrowsers().newBrowser(config).start();
    }

    @Given("^Browser - restart$")
    public void restart() {
        if (!CucumberPumpCore.instance().getBrowsers().has()) {
            throw new IllegalStateException("You have to start at least one browser before restart");
        }
        Browser browser = CucumberPumpCore.instance().getBrowsers().get();
        browser.close();
        core().getBrowsers().newBrowser(browser.getConfig()).start();
    }

    @Given("^Browser - close current$")
    public void closeCurrent() {
        if (core().getBrowsers().has()) {
            core().getBrowsers().get().close();
        }
    }

    @Given("^Browser - stop all in thread$")
    public void closeAllCurrentThread() {
        if (core().getBrowsers().has()) {
            core().getBrowsers().closeCurrentThread();
        }
    }

    @Given("^Browser - stop anyone$")
    public void closeAll() {
        core().getBrowsers().close();
    }
}
