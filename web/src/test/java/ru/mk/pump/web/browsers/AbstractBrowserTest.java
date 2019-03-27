package ru.mk.pump.web.browsers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.AbstractTestWithBrowser;

class AbstractBrowserTest extends AbstractTestWithBrowser {

    @AfterEach
    public void tearDown(){
        getBrowser().close();
        createBrowser();
    }

    @Test
    void close() {
        getBrowser().start();
        getBrowser().open("https://ya.ru");
        getBrowser().close();
    }

    @Test
    void start() {
        getBrowser().start();
        getBrowser().open("https://ya.ru");
        getBrowser().tabs().openTab();
        getBrowser().open("https://google.ru");
        getBrowser().close();
    }

    @Test
    @Disabled
    void windows() {

    }

}