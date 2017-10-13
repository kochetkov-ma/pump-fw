package ru.mk.pump.commons.utils;

import static ru.mk.pump.commons.constants.MainConstants.SCREEN_FORMAT;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.reporter.Screenshoter;

@Slf4j
public class DesktopScreenshoter implements Screenshoter {

    @Override
    public Optional<byte[]> getScreen() {
        log.info("Try to get desktop screen");
        BufferedImage originalImage = getScreenAsBufferedImage();
        if (originalImage == null) {
            return Optional.of(new byte[0]);
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(originalImage, SCREEN_FORMAT, outputStream);
            outputStream.flush();
            log.info("Getting desktop screen - success");
            return Optional.ofNullable(outputStream.toByteArray());
        } catch (IOException e) {
            log.error("Getting desktop screen - error", e);
            return Optional.of(new byte[0]);
        }
    }

    @Nullable
    private static BufferedImage getScreenAsBufferedImage() {
        try {
            return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException e) {
            log.error("Getting screen BufferedImage - error", e);
            return null;
        }
    }
}
