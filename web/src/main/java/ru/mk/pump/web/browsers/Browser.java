package ru.mk.pump.web.browsers;

import java.io.Closeable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;

public interface Browser extends Closeable {

    String getId();

    Browser start();

    @NotNull
    WebDriver getDriver();

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
