package ru.mk.pump.commons.reporter;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.model.Attachment;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReporterImpl implements Reporter {

    public enum Type {
        INFO(""), WARNING("[WARN] "), ERROR("[ERROR] "), ATTACHMENT("[ATTACHMENT] "), CHECK("[CHECK] ");

        @Getter
        private final String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }

    private final AttachmentsFactory attachmentsFactory;

    public ReporterImpl(Screenshoter screenshoter) {
        this.attachmentsFactory = new AttachmentsFactory(screenshoter);
    }

    /**
     *
     * @param level step level
     * @param title step title
     * @param description need to be title + lineSeparator + description for full information
     * @param throwable can be null
     * @param attachment can be null
     */
    @Step("{level}{title}")
    @Description("{description}")
    private void step(Type level, String title, String description, Throwable throwable, Attachment attachment) {

    }

    @Override
    public void info(String title, String description, Attachment attachment) {

    }

    @Override
    public void info(String title, String description) {

    }

    @Override
    public void error(String title, String description) {

    }

    @Override
    public void error(String title, String description, Attachment attachment) {

    }

    @Override
    public void error(String title, String description, Attachment attachment, Throwable throwable) {

    }

    @Override
    public void error(String title, String description, Throwable throwable) {

    }

    @Override
    public void attach(Attachment attachment) {

    }

    @Override
    public AttachmentsFactory attachments() {
        return attachmentsFactory;
    }
}
