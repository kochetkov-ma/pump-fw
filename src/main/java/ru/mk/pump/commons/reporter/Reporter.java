package ru.mk.pump.commons.reporter;

import lombok.NonNull;

@SuppressWarnings("unused")
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

    void fail(String title, String description, @NonNull Attachment attachment, @NonNull AssertionError assertionError);

    /**
     * @param attachment screen to report {@link AttachmentsFactory}. can be null
     */
    void pass(String title, String description, Attachment attachment);

    void pass(String title, String description);

    AttachmentsFactory attachments();

}
