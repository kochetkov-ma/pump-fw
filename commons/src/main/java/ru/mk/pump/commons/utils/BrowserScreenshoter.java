package ru.mk.pump.commons.utils;

import java.util.Optional;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import ru.mk.pump.commons.reporter.Screenshoter;

@Slf4j
public class BrowserScreenshoter implements Screenshoter {

    private final Supplier<WebDriver> driverSupplier;

    public BrowserScreenshoter(Supplier<WebDriver> driverSupplier) {
        this.driverSupplier = driverSupplier;
    }

    @Override
    public Optional<byte[]> getScreen() {
        try {
            log.info("WebDriver try to take screen");
            final Optional<byte[]> res = Optional.of(((TakesScreenshot) driverSupplier.get()).getScreenshotAs(OutputType.BYTES));
            log.info("WebDriver screen shoot is success");
            return res;
        } catch (UnreachableBrowserException ex) {
            log.error("Error when WebDriver taking screen", ex);
            return Optional.of(new byte[0]);
        }
    }
}
