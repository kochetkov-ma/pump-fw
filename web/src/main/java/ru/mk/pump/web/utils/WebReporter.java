package ru.mk.pump.web.utils;

import com.google.common.base.Preconditions;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.concurrent.ThreadSafe;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.reporter.ReporterAllure;
import ru.mk.pump.commons.reporter.ReporterAllure.Type;
import ru.mk.pump.commons.reporter.Screenshoter;
import ru.mk.pump.commons.utils.BrowserScreenshoter;

/**
 * Reporter singleton.
 * {@link WebReporter#init(Supplier)} before using
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@UtilityClass
@ThreadSafe
public class WebReporter {

    private final static Type DEFAULT_TYPE = Type.INFO;

    private final static Function<Supplier<WebDriver>, Screenshoter> DEFAULT_SCREEN = BrowserScreenshoter::new;

    private Reporter reporter;

    /**
     * Init {@link Screenshoter} and {@link Type} before using
     */
    public void init(@NotNull Screenshoter screenshoter, @NotNull Type loggerDuplicateLevel) {
        newReporterAndSave(screenshoter, loggerDuplicateLevel);
    }

    /**
     * Init web driver before start using.
     * And create default {@link Screenshoter} and {@link Type}
     */
    public void init(@NotNull Supplier<WebDriver> driver) {
        newReporterAndSave(DEFAULT_SCREEN.apply(driver), DEFAULT_TYPE);
    }

    public synchronized void setReporter(Reporter reporter) {
        WebReporter.reporter = reporter;
    }

    /**
     * {@link WebReporter#init(Supplier)} or {@link WebReporter#setReporter(Reporter)} before using
     */
    public synchronized Reporter getReporter() {
        Preconditions.checkNotNull(reporter, "Initialize before using");
        return reporter;
    }

    private void newReporterAndSave(Screenshoter screenshoter, Type loggerDuplicateLevel) {
        final Reporter reporter = new ReporterAllure(screenshoter, loggerDuplicateLevel);
        setReporter(reporter);
    }
}

