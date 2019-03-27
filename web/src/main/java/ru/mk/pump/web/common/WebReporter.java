package ru.mk.pump.web.common;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import org.openqa.selenium.WebDriver;
import ru.mk.pump.commons.reporter.AllureReporter;
import ru.mk.pump.commons.reporter.AllureReporter.Type;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.reporter.Screenshoter;
import ru.mk.pump.commons.utils.BrowserScreenshoter;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.web.common.api.WebListenersConfiguration;
import ru.mk.pump.web.configuration.ConfigurationHolder;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * {@link Reporter} and {@link Verifier} singleton.
 * Call {@link WebReporter#init(Supplier)} before using. Use this class if no need custom Reporter.
 */
@SuppressWarnings({"unused"})
public final class WebReporter {

    private static final Function<Supplier<WebDriver>, Screenshoter> DEFAULT_SCREEN = BrowserScreenshoter::new;
    private static final Object PR_LOCK = new Object();
    private static final Object V_LOCK = new Object();
    private static final Object R_LOCK = new Object();
    private static final Object WLC_LOCK = new Object();
    private static final WebReporter INSTANCE = new WebReporter();
    private static Reporter reporter;
    private static Verifier verifier;
    private static ProjectResources projectResources;
    private static WebListenersConfiguration listenersConfiguration;

    /**
     * First basic initialization.
     */
    private WebReporter() {
        newInstancesAndSave(Optional::empty, get(ConfigurationHolder.get().getReporting().getPostLogbackLevel(), Type.ALL));
    }

    /**
     * User initialization with screen shooter and logger level.
     */
    public static void init(@NonNull Screenshoter screenshoter, @NonNull Type loggerDuplicateLevel) {
        newInstancesAndSave(screenshoter, loggerDuplicateLevel);
    }

    /**
     * User initialization with lazy web driver.
     */
    public static void init(@NonNull Supplier<WebDriver> driver) {
        newInstancesAndSave(DEFAULT_SCREEN.apply(driver), get(ConfigurationHolder.get().getReporting().getPostLogbackLevel(), Type.ALL));
    }

    @NonNull
    public static Reporter getReporter() {
        synchronized (R_LOCK) {
            Preconditions.checkNotNull(projectResources,
                    "First of all you must init Reporter from your project");
            return reporter;
        }
    }

    public static void setReporter(@NonNull Reporter reporter) {
        synchronized (R_LOCK) {
            WebReporter.reporter = reporter;
        }
    }

    @NonNull
    public static Verifier getVerifier() {
        synchronized (V_LOCK) {
            Preconditions.checkNotNull(projectResources,
                    "First of all you must init Verifier from your project");
            return verifier;
        }
    }

    public static void setVerifier(@NonNull Verifier verifier) {
        synchronized (V_LOCK) {
            WebReporter.verifier = verifier;
        }
    }

    @NonNull
    public static ProjectResources getProjectResources() {
        synchronized (PR_LOCK) {
            Preconditions.checkNotNull(projectResources,
                    "First of all you must init ProjectResources from your project");
            return projectResources;
        }
    }

    public static void setProjectResources(@NonNull ProjectResources projectResources) {
        synchronized (PR_LOCK) {
            WebReporter.projectResources = projectResources;
        }
    }

    @Nullable
    public static WebListenersConfiguration getListenersConfiguration() {
        synchronized (WLC_LOCK) {
            return listenersConfiguration;
        }
    }

    public static void setListenersConfiguration(@Nullable WebListenersConfiguration listenersConfiguration) {
        synchronized (WLC_LOCK) {
            WebReporter.listenersConfiguration = listenersConfiguration;
        }
    }

    //region Private
    private static void newInstancesAndSave(Screenshoter screenshoter, Type loggerDuplicateLevel) {
        /*configure reporter*/
        final AllureReporter reporter = new AllureReporter(screenshoter, loggerDuplicateLevel);
        reporter.setAutoScreenLevel(get(ConfigurationHolder.get().getReporting().getPostScreenLevel(), Type.OFF));
        reporter.setPostingLevel(get(ConfigurationHolder.get().getReporting().getPostLevel(), Type.INFO));

        /*configure verifier*/
        final Verifier verifier = new Verifier(reporter);
        verifier.setPostPassedCheck(ConfigurationHolder.get().getVerify().isPostPassedCheck());
        verifier.setPostPassedScreen(ConfigurationHolder.get().getVerify().isScreenOnSuccessCheck());
        if (WebReporter.reporter == null) {
            setReporter(reporter);
        }
        if (WebReporter.verifier == null) {
            setVerifier(verifier);
        }
    }

    private static <T> T get(T object, T defaultIfNull) {
        if (object == null) {
            return defaultIfNull;
        } else {
            return object;
        }
    }
    //endregion
}