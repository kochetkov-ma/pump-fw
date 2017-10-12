package ru.mk.pump.commons.reporter;

import static ru.mk.pump.commons.constants.MainConstants.SCREEN_FORMAT;

import java.nio.file.Path;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.constants.MainConstants;
import ru.mk.pump.commons.utils.FileUtils;

@SuppressWarnings("unused")
@Slf4j
public class AttachmentsFactory {

    private static final String IMAGE = "image/" + SCREEN_FORMAT;

    private Screenshoter screenshoter;

    protected AttachmentsFactory(@NonNull Screenshoter screenshoter) {

        this.screenshoter = screenshoter;
    }

    public Attachment file(@NonNull String attachmentName, @NonNull Path path) {
        return new Attachment().withName(attachmentName).withSource(FileUtils.toString(path, MainConstants.FILE_ENCODING))
            .withExtension(FileUtils.getExtension(path));
    }

    public Attachment file(@NonNull String attachmentName, @NonNull Supplier<byte[]> bytes) {
        return new Attachment().withName(attachmentName).withSourceByte(bytes).withExtension("txt");
    }

    public Attachment screen(@NonNull String attachmentName, @NonNull Supplier<byte[]> bytes) {
        return new Attachment().withName(attachmentName).withSourceByte(bytes).withType(IMAGE)
            .withExtension(SCREEN_FORMAT);
    }

    public Attachment screen(@NonNull String attachmentName) {
        return new Attachment().withName(attachmentName).withSourceByte(() -> screenshoter.getScreen().orElse(new byte[0]))
            .withExtension("png")
            .withType(IMAGE);
    }
}
