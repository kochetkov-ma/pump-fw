package ru.mk.pump.commons.reporter;


import static ru.mk.pump.commons.constants.MainConstants.SCREEN_FORMAT;

import java.nio.file.Path;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.mk.pump.commons.constants.MainConstants;
import ru.mk.pump.commons.utils.FileUtils;


@SuppressWarnings({"unused", "WeakerAccess"})
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

    public Attachment text(@NonNull String attachmentName, @NonNull String text) {
        return new Attachment().withName(attachmentName).withSource(text)
                .withExtension("txt")
                .withType("text/plain");
    }

    public Attachment screen(@NonNull String attachmentName, @NonNull Supplier<byte[]> bytes) {
        return new Attachment().withName(attachmentName).withSourceByte(bytes)
                .withType(IMAGE)
                .withExtension(SCREEN_FORMAT);
    }

    /**
     * for skip auto screening
     */
    public Attachment dummy() {
        return new Attachment().withName("dummy").withSource("empty")
                .withType("dummy");
    }

    public Attachment screen(@NonNull String attachmentName) {
        return new Attachment().withName(attachmentName).withSourceByte(() -> screenshoter.getScreen().orElse(new byte[0]))
                .withExtension("png")
                .withType(IMAGE);
    }

    public static boolean isDummy(@Nullable Attachment attachment) {
        if (attachment == null){
            return false;
        }
        return StringUtils.containsIgnoreCase(attachment.getType(), "dummy");
    }

    public static boolean isScreen(@Nullable Attachment attachment){
        if (attachment == null){
            return false;
        }
        return StringUtils.containsIgnoreCase(attachment.getType(), "image");
    }
}
