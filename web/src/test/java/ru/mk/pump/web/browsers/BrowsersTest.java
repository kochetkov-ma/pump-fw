package ru.mk.pump.web.browsers;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.AbstractTestWithBrowser;

@Slf4j
public class BrowsersTest extends AbstractTestWithBrowser {

    @BeforeEach
    public void setUp() {
        setBrowsers(new Browsers());
    }

    @Test
    void newBrowser() throws InterruptedException, IllegalAccessException {
        Thread threadOne = new Thread(() -> {
            getBrowsers().newBrowser(getConfig());
            getBrowsers().newBrowser(getConfig());
            getBrowsers().newBrowser(getConfig());
            assertThat(getBrowsers().has()).isTrue();
            assertThat(getBrowsers().getBrowsers().size()).isEqualTo(3);
        }, "-1-"
        );
        threadOne.start();

        Thread threadTwo = new Thread(() -> {
            getBrowsers().newBrowser(getConfig());
            getBrowsers().newBrowser(getConfig());
            getBrowsers().closeCurrentThread();
            assertThat(getBrowsers().has()).isFalse();
            assertThat(getBrowsers().getBrowsers().size()).isEqualTo(2);
        }, "-2-"
        );
        threadTwo.start();

        threadTwo.join();
        threadOne.join();

        assertThat(getBrowsers().getBrowsers().size()).isEqualTo(0);
        assertThat((Set) FieldUtils.readField(getBrowsers(), "internalAllBrowsers", true)).hasSize(5);
    }

    @Test
    void get() {
        getBrowsers().newBrowser(getConfig());
        String one = getBrowsers().get().getId();
        log.debug(one);
        getBrowsers().newBrowser(getConfig());
        String two = getBrowsers().get().getId();
        log.debug(two);
        getBrowsers().newBrowser(getConfig());
        String three = getBrowsers().get().getId();
        log.debug(three);
        assertThat(one).isNotEqualTo(two).isNotEqualTo(three);
        assertThat(two).isNotEqualTo(three);
    }

    @Test
    void has() {
    }

    @Test
    void getLunchedBrowsers() {
    }

    @Test
    void close() {
    }

    @Test
    void closeCurrentThread() {
    }
}