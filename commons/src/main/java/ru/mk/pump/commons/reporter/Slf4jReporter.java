package ru.mk.pump.commons.reporter;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Str;

import java.nio.file.Path;
import java.util.function.Supplier;

@Slf4j
public class Slf4jReporter implements Reporter {
    private static final String LOG_PREFIX = "[REPORTER] STEP";
    private static final String MSG = LOG_PREFIX + " Title : {}\nDescription : {}";
    private static final String MSG_WITH_ATT = LOG_PREFIX + " Title : {}\nDescription : {}\nAttachment: {}";
    private static final String TEST_START = LOG_PREFIX + " [TEST START] Title : {}\nDescription : {}";
    private static final String TEST_STOP = LOG_PREFIX + " [TEST STOP]";
    private static final String BLOCK_START = LOG_PREFIX + " [BLOCK START] Title : {}\nDescription : {}";
    private static final String BLOCK_STOP = LOG_PREFIX + " [BLOCK STOP]";

    @Override
    public void testStart(String s, String s1) {
        log.info(TEST_START, s, s1);
    }

    @Override
    public void testStop() {
        log.info(TEST_STOP);
    }

    @Override
    public void info(String s, String s1, Attachment attachment) {
        log.info(MSG_WITH_ATT, s, s1, toString(attachment));
    }

    @Override
    public void info(String s, String s1) {
        log.info(MSG, s, s1);
    }

    @Override
    public void debug(String s, String s1) {
        log.debug(MSG, s, s1);
    }

    @Override
    public void debug(String s, String s1, Attachment attachment) {
        log.debug(MSG_WITH_ATT, s, s1, toString(attachment));
    }

    @Override
    public void blockStart(String s, String s1) {
        log.info(BLOCK_START, s, s1);
    }

    @Override
    public void blockStop() {
        log.info(BLOCK_STOP);
    }

    @Override
    public void blockStopAll() {
        log.info(BLOCK_STOP + " ALL");
    }

    @Override
    public void warn(String s, String s1, Attachment attachment, Throwable throwable) {
        log.warn(MSG_WITH_ATT, s, s1, toString(attachment));
        if (throwable != null) {
            log.warn("Throwable", throwable);
        }
    }

    @Override
    public void warn(String s, String s1, Throwable throwable) {
        log.warn(MSG, s, s1);
        if (throwable != null) {
            log.warn("Throwable", throwable);
        }
    }

    @Override
    public void warn(String s, String s1, Attachment attachment) {
        log.warn(MSG_WITH_ATT, s, s1, toString(attachment));
    }

    @Override
    public void warn(String s, String s1) {
        log.warn(MSG, s, s1);
    }

    @Override
    public void error(String s, String s1) {
        log.error(MSG, s, s1);
    }

    @Override
    public void error(String s, String s1, Attachment attachment) {
        log.error(MSG_WITH_ATT, s, s1, toString(attachment));
    }

    @Override
    public void error(String s, String s1, Attachment attachment, Throwable throwable) {
        log.error(MSG_WITH_ATT, s, s1, toString(attachment));
        if (throwable != null) {
            log.error("Throwable", throwable);
        }
    }

    @Override
    public void error(String s, String s1, Throwable throwable) {
        log.error(MSG_WITH_ATT, s, s1);
        if (throwable != null) {
            log.error("Throwable", throwable);
        }
    }

    @Override
    public void attach(Attachment attachment) {
        log.info(MSG_WITH_ATT, "attachment", "attachment", toString(attachment));
    }

    @Override
    public void fail(String s, String s1, @NonNull Attachment attachment, @NonNull AssertionError assertionError) {
        log.error(MSG_WITH_ATT, s, s1, toString(attachment));
        throw assertionError;
    }

    @Override
    public void pass(String s, String s1, Attachment attachment) {
        info("[PASS] " + Str.toString(s), s1, attachment);
    }

    @Override
    public void pass(String s, String s1) {
        info("[PASS] " + s, s1);
    }

    @Override
    public AttachmentFactory attachments() {
        return new Slf4JAttachmentFactory();
    }

    private static class Slf4JAttachmentFactory implements AttachmentFactory {

        private final static Attachment ATTACHMENT = new Attachment();

        @Override
        public Attachment file(@NonNull String attachmentName, @NonNull Path path) {
            return ATTACHMENT.withName(attachmentName).withSource(path.toString()).withType("file");
        }

        @Override
        public Attachment file(@NonNull String attachmentName, @NonNull Supplier<byte[]> bytes) {
            return ATTACHMENT.withName(attachmentName).withSourceByte(bytes).withType("bytes");
        }

        @Override
        public Attachment text(@NonNull String attachmentName, @NonNull String text) {
            return ATTACHMENT.withName(attachmentName).withSource(text).withType("text");
        }

        @Override
        public Attachment screen(@NonNull String attachmentName, @NonNull Supplier<byte[]> bytes) {
            return ATTACHMENT.withName(attachmentName).withSourceByte(bytes).withType("screen");
        }

        @Override
        public Attachment dummy() {
            return ATTACHMENT;
        }

        @Override
        public Attachment screen(@NonNull String attachmentName) {
            return ATTACHMENT.withName(attachmentName).withSourceByte(() -> new byte[0]).withType("screen");
        }
    }

    private String toString(Attachment attachment) {
        return attachment != null ? attachment.toPrettyString() : "null";
    }
}