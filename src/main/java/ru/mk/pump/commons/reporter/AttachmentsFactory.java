package ru.mk.pump.commons.reporter;

import io.qameta.allure.model.Attachment;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.constants.MainConstants;
import ru.mk.pump.commons.utils.FileUtils;

@Slf4j
public class AttachmentsFactory {

    private Screenshoter screenshoter;

    public AttachmentsFactory(Screenshoter screenshoter) {

        this.screenshoter = screenshoter;
    }

    public Attachment file(String attachmentName, Path path) {
        return new Attachment().withName(attachmentName).withSource(FileUtils.toString(path, MainConstants.FILE_ENCODING));
    }

    public Attachment file(String attachmentName, byte[] bytes) {
        return new Attachment().withName(attachmentName).withSource(new String(bytes, MainConstants.FILE_ENCODING));
    }

    public Attachment screen(String attachmentName, byte[] bytes) {
        return new Attachment().withName(attachmentName).withSource(new String(bytes, MainConstants.FILE_ENCODING));
    }

    public Attachment screen(String attachmentName) {
        return new Attachment().withName(attachmentName).withSource(new String(screenshoter.getScreen().orElse(new byte[0]), MainConstants.FILE_ENCODING));
    }
}
