package ru.mk.pump.cucumber.steps.web;

import cucumber.api.java.en.Given;
import org.openqa.selenium.Alert;
import ru.mk.pump.cucumber.CucumberCore;
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

    @Given("^Browser - reset frame$")
    public void setRootFrame() {
        CucumberCore.instance().getBrowsers().get().actions().switchToRootFrame();
    }

    @Given("^Browser - restart$")
    public void restart() {
        if (!CucumberCore.instance().getBrowsers().has()) {
            throw new IllegalStateException("You have to startScenario at least one browser before restart");
        }
        Browser browser = CucumberCore.instance().getBrowsers().get();
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

    @Given("^Alert - save text to 'result'$")
    public void alertText() {
        Alert alert = core().getBrowsers().get().actions().alert(5);
        core().getTestVariables().putResult(alert.getText());
    }

    @Given("^Alert - accept$")
    public void alertAccept() {
        core().getBrowsers().get().actions().alert(5).accept();
    }

    @Given("^Alert - dismiss$")
    public void alertDismiss() {
        core().getBrowsers().get().actions().alert(5).dismiss();
    }
}