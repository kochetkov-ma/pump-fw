package ru.mk.pump.commons.reporter;

import lombok.NonNull;

import java.nio.file.Path;
import java.util.function.Supplier;

public interface AttachmentFactory {
    Attachment file(@NonNull String attachmentName, @NonNull Path path);

    Attachment file(@NonNull String attachmentName, @NonNull Supplier<byte[]> bytes);

    Attachment text(@NonNull String attachmentName, @NonNull String text);

    Attachment screen(@NonNull String attachmentName, @NonNull Supplier<byte[]> bytes);

    Attachment dummy();

    Attachment screen(@NonNull String attachmentName);
}
