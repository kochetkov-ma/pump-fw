package ru.mk.pump.web.browsers;

import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;
import ru.mk.pump.web.elements.internal.impl.AbstractWebTest;

public class AbstractBrowserTest extends AbstractWebTest{

    @Test
    public void start() throws IOException {
        browser.start();
        browser.open("https://ya.ru");
        browser.windows().newTab();
        browser.open("https://google.ru");
        browser.close();
    }

    @Test
    @Disabled
    public void windows() {

    }

}