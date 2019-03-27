package ru.mk.pump.cucumber.glue.step.ru;

import cucumber.api.java.en.Given;
import org.openqa.selenium.Alert;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.cucumber.glue.AbstractSteps;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.configuration.ConfigurationHolder;

public class BrowserSteps extends AbstractSteps {

    @Given("^браузер запустился $")
    public void newBrowser() {
        core().getBrowsers().newBrowser(ConfigurationHolder.get().getBrowserConfig());
    }

    @Given("^браузер (.+?) запустился $")
    public void newBrowser(BrowserType browserType) {
        final BrowserConfig config = ConfigurationHolder.get().getBrowserConfig();
        config.setType(browserType);
        core().getBrowsers().newBrowser(config).start();
    }

    @Given("^браузер вернулся на главный фрейм$")
    public void setRootFrame() {
        CucumberCore.instance().getBrowsers().get().actions().switchToRootFrame();
    }

    @Given("^браузер перезапустился$")
    public void restart() {
        if (!CucumberCore.instance().getBrowsers().has()) {
            throw new IllegalStateException("You have to startScenario at least one browser before restart");
        }
        Browser browser = CucumberCore.instance().getBrowsers().get();
        browser.close();
        core().getBrowsers().newBrowser(browser.getConfig()).start();
    }

    @Given("^браузер остановлен")
    public void closeCurrent() {
        if (core().getBrowsers().has()) {
            core().getBrowsers().get().close();
        }
    }

    @Given("^браузер остановлен в текущем потоке$")
    public void closeAllCurrentThread() {
        if (core().getBrowsers().has()) {
            core().getBrowsers().closeCurrentThread();
        }
    }

    @Given("^браузеры все остановлены$")
    public void closeAll() {
        core().getBrowsers().close();
    }

    @Given("^алерт сохранен текст в (.+?|result)$")
    public void alertText(String key) {
        Alert alert = core().getBrowsers().get().actions().alert(5);
        core().getTestVariables().put(key, alert.getText());
    }

    @Given("^алерт (принят|отклонен)$")
    public void alertAccept(String acceptOrDismiss) {
        if ("принять".equals(acceptOrDismiss)) {
            core().getBrowsers().get().actions().alert(5).accept();
        } else {
            core().getBrowsers().get().actions().alert(5).dismiss();
        }
    }
}