package ru.mk.pump.cucumber.plugin;

import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;

import java.util.Deque;

public class BrowserManager implements CucumberListener {

    private final boolean oneBrowserAllFeature;
    private final Browsers browsers;
    private final BrowserConfig browserConfig;

    BrowserManager(Browsers browsers, BrowserConfig browserConfig, boolean oneBrowserAllFeature) {
        this.browsers = browsers;
        this.browserConfig = browserConfig;
        this.oneBrowserAllFeature = oneBrowserAllFeature;
    }

    @Override
    public void onStartTest(CucumberMonitor monitor) {
        /*nothing*/
    }

    @Override
    public void onStartFeature(CucumberMonitor monitor) {
        /*single browser testing*/
        if (oneBrowserAllFeature) {
            /*если не создано ни одного браузера*/
            if (!browsers.has()) {
                browsers.newBrowser(browserConfig);
            } else {
                /*если созданный браузер закрыт*/
                if (browsers.get().isClosed()) {
                    Deque<Browser> browserDeque = browsers.getStartedBrowsers();
                    /*если очередь стартованных браузеров пуста*/
                    if (browserDeque.isEmpty()) {
                        browsers.newBrowser(browserConfig);
                    } else {
                        /*найдены стартованные браузеры - установить последний стартованный браузер в качестве текущего*/
                        browsers.setBrowser(browserDeque.getLast());
                    }
                }
            }
        } else {
            /*закрыть все браузеры данного потока*/
            browsers.closeCurrentThread();
            /*создать новый браузер*/
            browsers.newBrowser(browserConfig);
        }
        /*старт браузера*/
        browsers.get().start();
    }

    @Override
    public void onStartScenario(CucumberMonitor monitor) {
        /*nothing*/
    }

    @Override
    public void onFinishTest(CucumberMonitor monitor) {
        /*закрыть менеджер браузеров. Конец выполнения*/
        browsers.close();
    }

    @Override
    public void onFinishFeature(CucumberMonitor monitor) {
        /*закрыть все браузеры, если не включена настройка 'single browser testing'*/
        if (!oneBrowserAllFeature) {
            browsers.closeCurrentThread();
        }
    }

    @Override
    public void onFinishScenario(CucumberMonitor monitor) {
        /*nothing*/
    }
}