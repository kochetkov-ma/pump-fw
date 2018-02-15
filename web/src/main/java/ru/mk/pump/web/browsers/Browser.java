package ru.mk.pump.web.browsers;

import java.io.Closeable;
import java.util.function.Supplier;
import lombok.NonNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Browser extends Closeable, StrictInfo, Supplier<WebDriver> {

    String getId();

    Browser start();

    @NonNull
    WebDriver getDriver();

    default WebDriver get() {
        return getDriver();
    }

    BrowserConfig getConfig();

    void close();

    boolean isStarted();

    boolean isClosed();

    Browser open(String url);

    Navigation navigate();

    Browser refresh();

    WindowManager windows();

    DownloadManager downloads();

    LogManager logs();

    BrowserActions actions();

}
