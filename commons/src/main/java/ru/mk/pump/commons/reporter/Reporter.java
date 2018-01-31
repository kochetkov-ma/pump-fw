package ru.mk.pump.commons.reporter;

import lombok.NonNull;

@SuppressWarnings("unused")
public interface Reporter {

    void testStart(String title, String description);

    void testStop();

    /**
     * @param attachment {@link #attachments()}
     */
    void info(String title, String description, Attachment attachment);

    void info(String title, String description);

    void blockStart(String title, String description);

    void blockStop();

    void blockStopAll();

    /**
     * @param attachment {@link #attachments()}
     */
    void warn(String title, String description, Attachment attachment, Throwable throwable);

    void warn(String title, String description, Throwable throwable);

    void warn(String title, String description, Attachment attachment);

    void warn(String title, String description);

    void error(String title, String description);

    /**
     * @param attachment {@link #attachments()}
     */
    void error(String title, String description, Attachment attachment);

    /**
     * @param attachment {@link #attachments()}
     */
    void error(String title, String description, Attachment attachment, Throwable throwable);

    void error(String title, String description, Throwable throwable);

    /**
     * @param attachment {@link #attachments()}
     */
    void attach(Attachment attachment);

    /**
     * @param attachment {@link #attachments()}
     */
    void fail(String title, String description, @NonNull Attachment attachment, @NonNull AssertionError assertionError);

    /**
     * @param attachment screen to report {@link AttachmentsFactory}. can be null
     */
    void pass(String title, String description, Attachment attachment);

    void pass(String title, String description);

    AttachmentsFactory attachments();

}
