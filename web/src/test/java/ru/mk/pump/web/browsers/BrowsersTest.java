package ru.mk.pump.web.browsers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;

@Slf4j
public class BrowsersTest {

    private Browsers browsers;

    private BrowserConfig config;

    @BeforeEach
    public void setUp() throws Exception {
        browsers = new Browsers();
        config = new BrowserConfig(false, Size.of(true), BrowserType.CHROME);
    }

    @Test
    public void newBrowser() throws InterruptedException, IllegalAccessException {
        Thread threadOne = new Thread(() -> {
            browsers.newBrowser(config);
            browsers.newBrowser(config);
            browsers.newBrowser(config);
            assertThat(browsers.has()).isTrue();
            assertThat(browsers.getBrowsers().size()).isEqualTo(3);
        }, "-1-"
        );
        threadOne.start();

        Thread threadTwo = new Thread(() -> {
            browsers.newBrowser(config);
            browsers.newBrowser(config);
            browsers.closeCurrentThread();
            assertThat(browsers.has()).isFalse();
            assertThat(browsers.getBrowsers().size()).isEqualTo(2);
        }, "-2-"
        );
        threadTwo.start();

        threadTwo.join();
        threadOne.join();

        assertThat((List) FieldUtils.readField(browsers, "internalAllBrowsers", true)).hasSize(5);
    }

    @Test
    public void get() {
        browsers.newBrowser(config);
        String one = browsers.get().getId();
        log.debug(one);
        browsers.newBrowser(config);
        String two = browsers.get().getId();
        log.debug(two);
        browsers.newBrowser(config);
        String three = browsers.get().getId();
        log.debug(three);
        assertThat(one).isNotEqualTo(two).isNotEqualTo(three);
        assertThat(two).isNotEqualTo(three);
    }

    @Test
    public void has() {
    }

    @Test
    public void getLunchedBrowsers() {
    }

    @Test
    public void close() {
    }

    @Test
    public void closeCurrentThread() {
    }

    @AfterEach
    public void tearDown() throws Exception {
        browsers.close();
    }
}