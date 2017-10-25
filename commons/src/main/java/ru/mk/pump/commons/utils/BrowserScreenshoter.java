package ru.mk.pump.commons.utils;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import ru.mk.pump.commons.reporter.Screenshoter;

@Slf4j
public class BrowserScreenshoter implements Screenshoter {

    private final WebDriver driver;

    public BrowserScreenshoter(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public Optional<byte[]> getScreen() {
        try {
            log.info("WebDriver try to take screen");
            return Optional.of(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        } catch (UnreachableBrowserException ex) {
            log.error("error WebDriver taking screen", ex);
            return Optional.of(new byte[0]);
        }
    }
}
