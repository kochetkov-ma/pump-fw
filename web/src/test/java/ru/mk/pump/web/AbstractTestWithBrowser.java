package ru.mk.pump.web;

import com.google.common.base.Enums;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.mk.pump.commons.utils.EnvVariables;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.elements.internal.ElementWaiter;
import ru.mk.pump.web.utils.WebReporter;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractTestWithBrowser {

    private static final BrowserType DEFAULT_BROWSER_TYPE = ConfigurationHolder.get().getBrowserConfig().getType();

    @Getter
    @Setter
    private static Browsers browsers;

    @Getter
    @Setter
    private static BrowserConfig config;

    @Getter
    @Setter
    private Browser browser;

    public static BrowserType getTestBrowserType() {

        return Enums.getIfPresent(BrowserType.class, EnvVariables.get("TEST_BROWSER_TYPE", DEFAULT_BROWSER_TYPE.name())).or(DEFAULT_BROWSER_TYPE);
    }

    @AfterAll
    public static void afterAll() {
        browsers.close();
    }

    @BeforeAll
    public static void beforeAll() {
        config = BrowserConfig.of(Size.of(true), getTestBrowserType());
        browsers = new Browsers();
        if (getTestBrowserType() == BrowserType.CHROME) {
            config.setWebDriverPath(ProjectResources.findResource("chromedriver.exe").toString());
        }
    }

    @BeforeEach
    public void setUp() {
        if (!browsers.has()) {
            createBrowser().start();
        } else {
            browser = browsers.get();
        }
    }

    @AfterEach
    public void tearDown() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 10;
    }

    /**
     * Создать браузер и записать в поле {@link #browser}. Но не запускать
     * @return Созданный браузер
     */
    protected Browser createBrowser() {
        this.browser = browsers.newBrowser(config);
        WebReporter.init(browser);
        return browser;
    }
}
