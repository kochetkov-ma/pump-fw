package ru.mk.pump.web.common;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebDriver;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.reporter.ReporterAllure;
import ru.mk.pump.commons.reporter.ReporterAllure.Type;
import ru.mk.pump.commons.reporter.Screenshoter;
import ru.mk.pump.commons.utils.BrowserScreenshoter;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.web.common.api.WebListenersConfiguration;
import ru.mk.pump.web.configuration.ConfigurationHolder;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * {@link Reporter} and {@link Verifier} singleton.
 * Call {@link WebReporter#init(Supplier)} before using. Use this class if no need custom Reporter.
 */
@SuppressWarnings( {"WeakerAccess", "unused"})
@UtilityClass
@ThreadSafe
public class WebReporter {

    private final static Function<Supplier<WebDriver>, Screenshoter> DEFAULT_SCREEN = BrowserScreenshoter::new;

    private ThreadLocal<Reporter> reporter = new InheritableThreadLocal<>();

    private ThreadLocal<Verifier> verifier = new InheritableThreadLocal<>();

    /**
     * Set listeners configuration. You must implement {@link WebListenersConfiguration} to use custom listeners instead default.
     */
    @Getter
    @Setter
    private WebListenersConfiguration listenersConfiguration;

    static {
        /*fake init without screens*/
        newInstancesAndSave(Optional::empty, get(ConfigurationHolder.get().getReporting().getPostLogbackLevel(), Type.ALL));
    }

    /**
     * Init {@link Screenshoter} and {@link Type} before using
     */
    public void init(@NonNull Screenshoter screenshoter, @NonNull Type loggerDuplicateLevel) {
        newInstancesAndSave(screenshoter, loggerDuplicateLevel);
    }

    /**
     * Init web driver before start using.
     * And create default {@link Screenshoter} and {@link Type}
     */
    public void init(@NonNull Supplier<WebDriver> driver) {
        newInstancesAndSave(DEFAULT_SCREEN.apply(driver), get(ConfigurationHolder.get().getReporting().getPostLogbackLevel(), Type.ALL));
    }

    public synchronized void setReporter(Reporter reporter) {
        WebReporter.reporter.set(reporter);
    }

    public synchronized void setVerifier(Verifier verifier) {
        WebReporter.verifier.set(verifier);
    }

    /**
     * {@link WebReporter#init(Supplier)} or {@link WebReporter#setReporter(Reporter)} before using
     */
    public Reporter getReporter() {
        Preconditions.checkNotNull(reporter.get(), "Initialize before using");
        return reporter.get();
    }

    /**
     * {@link WebReporter#init(Supplier)} or {@link WebReporter#setVerifier(Verifier)} before using
     */
    public Verifier getVerifier() {
        Preconditions.checkNotNull(verifier.get(), "Initialize before using");
        return verifier.get();
    }

    private void newInstancesAndSave(Screenshoter screenshoter, Type loggerDuplicateLevel) {
        /*configure reporter*/
        final ReporterAllure reporter = new ReporterAllure(screenshoter, loggerDuplicateLevel);
        reporter.setAutoScreenLevel(get(ConfigurationHolder.get().getReporting().getPostScreenLevel(), Type.OFF));
        reporter.setPostingLevel(get(ConfigurationHolder.get().getReporting().getPostLevel(), Type.INFO));

        /*configure verifier*/
        final Verifier verifier = new Verifier(reporter);
        verifier.setPostPassedCheck(ConfigurationHolder.get().getVerify().isPostPassedCheck());
        verifier.setPostPassedScreen(ConfigurationHolder.get().getVerify().isScreenOnSuccessCheck());

        setReporter(reporter);
        setVerifier(verifier);
    }

    private static <T> T get(T object, T defaultIfNull) {
        if (object == null) {
            return defaultIfNull;
        } else {
            return object;
        }
    }
}