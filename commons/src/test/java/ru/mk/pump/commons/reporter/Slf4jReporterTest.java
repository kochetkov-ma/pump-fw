package ru.mk.pump.commons.reporter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class Slf4jReporterTest {

    private static Slf4jReporter reporter;
    private ArgumentCaptor<ILoggingEvent> argumentCaptor;
    private Appender<ILoggingEvent> mockedAppender;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void before() {
        this.mockedAppender = Mockito.mock(Appender.class);
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).addAppender(mockedAppender);
        this.argumentCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
    }

    @BeforeAll
    static void beforeAll() {
        reporter = new Slf4jReporter();
    }

    @Test
    void testStart() {
        reporter.testStart("title", "description");
        reporter.testStart(null, null);
        val events = getAndVerifyEvents(2);
        assertEvent(events.get(0), Level.INFO, "[REPORTER] STEP [TEST START]", "Title : title", "Description : description");
        assertEvent(events.get(1), Level.INFO, "[REPORTER] STEP [TEST START]", "Title : null", "Description : null");
    }

    @SuppressWarnings("unchecked")
    private List<ILoggingEvent> getAndVerifyEvents(int times) {
        verify(mockedAppender, times(times)).doAppend(argumentCaptor.capture());
        Mockito.clearInvocations(mockedAppender);
        return argumentCaptor.getAllValues();
    }

    private void assertEvent(ILoggingEvent event, Level level, @NonNull String... containsSequence) {
        assertThat(event.getLevel()).isEqualTo(level);
        assertThat(event.getFormattedMessage()).containsSubsequence(containsSequence);
    }

    @Test
    void testStop() {
        reporter.testStop();
        val events = getAndVerifyEvents(1);
        assertEvent(events.get(0), Level.INFO, "[REPORTER] STEP [TEST STOP]");
    }

    @Test
    void info() {
        reporter.info("title", "description", reporter.attachments().file("test bytes", () -> new byte[]{1}));
        reporter.info("title", "description", reporter.attachments().file("test bytes", () -> null));
        reporter.info(null, null, null);
        reporter.info(null, null);
        val events = getAndVerifyEvents(4);
        assertEvent(events.get(0), Level.INFO, "[REPORTER] STEP Title", "title", "description", "test bytes", "Bytes size: '1'", "Type: 'bytes'");
        assertEvent(events.get(1), Level.INFO, "[REPORTER] STEP Title", "Type: 'bytes'");
        assertEvent(events.get(2), Level.INFO, "[REPORTER] STEP Title", "null", "null", "null");
        assertEvent(events.get(3), Level.INFO, "[REPORTER] STEP Title", "null", "null");
    }

    @Test
    void debug() {
        reporter.debug("title", "description", reporter.attachments().text("test text", "attachment text"));
        reporter.debug("title", "description", reporter.attachments().screen("test screen"));
        reporter.debug(null, null, null);
        reporter.debug(null, null);
        val events = getAndVerifyEvents(4);
        assertEvent(events.get(0), Level.DEBUG, "[REPORTER] STEP Title", "title", "description", "test text", "attachment text", "Type: 'text'");
        assertEvent(events.get(1), Level.DEBUG, "[REPORTER] STEP Title", "test screen", " Source: 'null'", "Type: 'screen'");
        assertEvent(events.get(2), Level.DEBUG, "[REPORTER] STEP Title", "null", "null", "null");
        assertEvent(events.get(3), Level.DEBUG, "[REPORTER] STEP Title", "null", "null");
    }

    @Test
    void blockStart() {
        reporter.blockStart("title", "description");
        reporter.blockStart(null, null);
        val events = getAndVerifyEvents(2);
        assertEvent(events.get(0), Level.INFO, "[REPORTER] STEP [BLOCK START]", "title", "description");
        assertEvent(events.get(1), Level.INFO, "[REPORTER] STEP [BLOCK START]", "null", "null");
    }

    @Test
    void blockStop() {
        reporter.blockStop();
        val events = getAndVerifyEvents(1);
        assertEvent(events.get(0), Level.INFO, "[REPORTER] STEP [BLOCK STOP]");
    }

    @Test
    void blockStopAll() {
        reporter.blockStopAll();
        val events = getAndVerifyEvents(1);
        assertEvent(events.get(0), Level.INFO, "[REPORTER] STEP [BLOCK STOP] ALL");
    }

    @Test
    void warn() {
        reporter.warn("title", "description", reporter.attachments().file("test file", Paths.get("fake path")), new Throwable("throw"));
        reporter.warn("title", "description", reporter.attachments().file("test file", Paths.get("fake path")), null);
        reporter.warn(null, null, null, null);
        val events = getAndVerifyEvents(4);
        assertEvent(events.get(0), Level.WARN, "[REPORTER] STEP Title", "title", "description", "test file", "fake path", "Type: 'file'");
        assertEvent(events.get(1), Level.WARN, "Throwable");
        assertEvent(events.get(0), Level.WARN, "[REPORTER] STEP Title", "title", "description", "test file", "fake path", "Type: 'file'");
        assertEvent(events.get(3), Level.WARN, "[REPORTER] STEP Title", "null", "null", "null");
    }

    @Test
    void error() {
        reporter.error("title", "description", reporter.attachments().file("test file", Paths.get("fake path")), new Throwable("throw"));
        reporter.error("title", "description", reporter.attachments().file("test file", Paths.get("fake path")), null);
        reporter.error(null, null, null, null);
        val events = getAndVerifyEvents(4);
        assertEvent(events.get(0), Level.ERROR, "[REPORTER] STEP Title", "title", "description", "test file", "fake path", "Type: 'file'");
        assertEvent(events.get(1), Level.ERROR, "Throwable");
        assertEvent(events.get(0), Level.ERROR, "[REPORTER] STEP Title", "title", "description", "test file", "fake path", "Type: 'file'");
        assertEvent(events.get(3), Level.ERROR, "[REPORTER] STEP Title", "null", "null", "null");
    }

    @Test
    void attach() {
        reporter.attach(reporter.attachments().dummy());
        val events = getAndVerifyEvents(1);
        assertEvent(events.get(0), Level.INFO, "[REPORTER] STEP Title : attachment", "attachment", "null", "null", "0", "null", "null");
    }

    @Test
    void fail() {
        assertThatThrownBy(
                () -> reporter.fail("title", "description", reporter.attachments().file("test file", Paths.get("fake path")), new AssertionError("assert")))
                .hasMessage("assert").isInstanceOf(AssertionError.class);
        val events = getAndVerifyEvents(1);
        assertEvent(events.get(0), Level.ERROR, "[REPORTER] STEP Title", "title", "description", "test file", "fake path", "Type: 'file'");
    }

    @Test
    void pass() {
        reporter.pass("title", "description", reporter.attachments().file("test bytes", () -> new byte[]{1}));
        reporter.pass("title", "description", reporter.attachments().file("test bytes", () -> null));
        reporter.pass(null, null, null);
        reporter.pass(null, null);
        val events = getAndVerifyEvents(4);
        assertEvent(events.get(0), Level.INFO, "[REPORTER] STEP Title", "[PASS] title", "description", "test bytes", "Bytes size: '1'", "Type: 'bytes'");
        assertEvent(events.get(1), Level.INFO, "[REPORTER] STEP Title", "[PASS]", "Type: 'bytes'");
        assertEvent(events.get(2), Level.INFO, "[REPORTER] STEP Title", "[PASS]", "null", "null", "null");
        assertEvent(events.get(3), Level.INFO, "[REPORTER] STEP Title", "[PASS]", "null", "null");
    }
}