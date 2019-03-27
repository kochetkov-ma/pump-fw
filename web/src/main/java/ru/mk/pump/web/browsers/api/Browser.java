package ru.mk.pump.web.browsers.api;

import lombok.NonNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.BrowserActions;
import ru.mk.pump.web.browsers.DownloadManager;
import ru.mk.pump.web.browsers.LogManager;
import ru.mk.pump.web.browsers.TabManager;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;

import java.io.Closeable;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Browser extends Closeable, StrictInfo, Supplier<WebDriver>, PrettyPrinter {

    String getId();

    Browser start();

    @NonNull WebDriver getDriver();

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

    TabManager tabs();

    DownloadManager downloads();

    LogManager logs();

    BrowserActions actions();

    Browser setSize(Dimension dimension);

    Browser maximize();

    @Override
    default String toPrettyString() {
        return PumpMessage.of(this).toPrettyString();
    }
}
