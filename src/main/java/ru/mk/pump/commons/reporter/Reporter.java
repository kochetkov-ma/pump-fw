package ru.mk.pump.commons.reporter;

import io.qameta.allure.model.Attachment;

public interface Reporter {

    /**
     * @param attachment {@link AttachmentsFactory}
     */
    void info(String title, String description, Attachment attachment);

    void info(String title, String description);

    void error(String title, String description);

    /**
     * @param attachment {@link AttachmentsFactory}
     */
    void error(String title, String description, Attachment attachment);

    /**
     * @param attachment {@link AttachmentsFactory}
     */
    void error(String title, String description, Attachment attachment, Throwable throwable);

    void error(String title, String description, Throwable throwable);

    /**
     * @param attachment {@link AttachmentsFactory}
     */
    void attach(Attachment attachment);

    AttachmentsFactory attachments();

}
