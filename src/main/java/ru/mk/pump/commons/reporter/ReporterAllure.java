package ru.mk.pump.commons.reporter;


import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.io.ByteArrayInputStream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.exception.ExecutionException;
import ru.mk.pump.commons.exception.ThrowableMessage;
import ru.mk.pump.commons.utils.StringUtils;

@SuppressWarnings("unused")
@Slf4j
public class ReporterAllure implements Reporter {

    @AllArgsConstructor
    private class Info {

        private static final String I = "kochetkov-ma@yandex.ru";

        private Attachment attachment;

        private Throwable throwable;

        Attachment attachment() {
            return attachment;
        }

        Throwable throwable() {
            return throwable;
        }

        @Override
        public String toString() {
            return StringUtils
                .oneLineConcat(I, attachment != null ? "Attachment exists" : null, throwable != null ? "Throwable: " + throwable.getMessage() : null);

        }
    }

    public enum Type {
        INFO(""), WARNING("[WARN] "), ERROR("[ERROR] "), ATTACHMENT("[ATTACHMENT] "), PASS("[PASS] "), FAIL("[FAIL] ");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name();
        }
    }

    private final AttachmentsFactory attachmentsFactory;

    public ReporterAllure(Screenshoter screenshoter) {
        this.attachmentsFactory = new AttachmentsFactory(screenshoter);
    }

    /**
     * @param level step level
     * @param title step title
     * @param description need to be title + lineSeparator + description for full information
     */
    @Step("{level.value}{title}")
    private void step(Type level, String title, String description, Info info) {
        if (info.attachment() != null) {
            attach(info.attachment());
        }
        if (info.throwable() != null) {
            if (info.throwable() instanceof AssertionError) {
                throw (AssertionError) info.throwable();
            } else {
                final ThrowableMessage exceptionMessage = new ThrowableMessage(title, description)
                    .addExtraInfo("Level", level.name())
                    .addExtraInfo("Attachment",
                        info.attachment() != null ? StringUtils.oneLineConcat(info.attachment().getName(), info.attachment().getType()) : null);
                throw new ExecutionException(exceptionMessage, info.throwable());
            }
        }
    }

    @Override
    public void info(String title, String description, Attachment attachment) {
        step(Type.INFO, title, description, new Info(attachment, null));
    }

    @Override
    public void info(String title, String description) {
        step(Type.INFO, title, description, new Info(null, null));
    }

    @Override
    public void fail(String title, String description, Attachment attachment, AssertionError assertionError) {
        step(Type.FAIL, title, description, new Info(attachment, assertionError));
    }

    @Override
    public void pass(String title, String description, Attachment attachment) {
        step(Type.PASS, title, description, new Info(attachment, null));
    }

    @Override
    public void pass(String title, String description) {
        step(Type.PASS, title, description, new Info(null, null));
    }

    @Override
    public void error(String title, String description) {
        step(Type.ERROR, title, description, new Info(null, new AssertionError("Test error")));
    }

    @Override
    public void error(String title, String description, Attachment attachment) {
        step(Type.ERROR, title, description, new Info(attachment, new AssertionError("Test error")));
    }

    @Override
    public void error(String title, String description, Attachment attachment, Throwable throwable) {
        step(Type.ERROR, title, description, new Info(attachment, throwable));
    }

    @Override
    public void error(String title, String description, Throwable throwable) {
        step(Type.ERROR, title, description, new Info(null, throwable));
    }

    @Override
    public void attach(Attachment attachment) {
        if (attachment.getSourceByte() != null) {
            Allure.addAttachment(attachment.getName(), attachment.getType(), new ByteArrayInputStream(attachment.getSourceByte().get()),
                attachment.getExtension());
        } else {
            Allure.addAttachment(attachment.getName(), attachment.getType(), attachment.getSource(), attachment.getExtension());
        }
    }

    @Override
    public AttachmentsFactory attachments() {
        return attachmentsFactory;
    }
}
