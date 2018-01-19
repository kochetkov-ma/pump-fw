package ru.mk.pump.web.browsers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.AbstractTestWithBrowser;

class AbstractBrowserTest extends AbstractTestWithBrowser {

    @Test
    void start() {
        getBrowser().start();
        getBrowser().open("https://ya.ru");
        getBrowser().windows().newTab();
        getBrowser().open("https://google.ru");
        getBrowser().close();
    }

    @Test
    @Disabled
    void windows() {

    }

}