package ru.mk.pump.web.browsers;

import java.util.UUID;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import ru.mk.pump.commons.exception.ThrowableMessage;
import ru.mk.pump.web.common.AbstractActivity;
import ru.mk.pump.web.common.Activity;
import ru.mk.pump.web.exceptions.BrowserException;

/**
 * Created by kochetkov-ma on 5/31/17.
 */
@ToString(of = {"id", "config"}, callSuper = true)
public abstract class AbstractBrowser extends AbstractActivity implements Browser {

    //region FINALS
    private final DriverBuilder builder;

    private final BrowserConfig config;

    private final String id;

    private final WindowManager windows;

    private final DownloadManager downloads;

    private final TabManager tabs;

    private final BrowserActions actions;

    private final LogManager logs;
    //endregion

    private WebDriver driver = null;

    //region INIT
    AbstractBrowser(@NotNull DriverBuilder builder, @NotNull BrowserConfig browserConfig, @NotNull UUID uuid) {
        super(uuid);
        this.builder = builder;
        this.config = browserConfig;
        this.id = browserConfig.getType().name() + "_" + uuid;
        this.windows = new WindowManager();
        this.tabs = new TabManager();
        this.downloads = new DownloadManager();
        this.actions = new BrowserActions();
        this.logs = new LogManager();

    }
    //endregion

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Browser start() {
        this.driver = builder.setConfig(config).createAndStartDriver();
        if (driver == null) {
            throw new BrowserException(new ThrowableMessage("Cannot start browser. Incorrect driver builder", toString()));
        }
        activate();
        return this;
    }

    @Override
    public WebDriver getDriver() {
        if (driver == null) {
            throw new BrowserException(new ThrowableMessage("Cannot get WebDriver - browser was not started or closed", toString()));
        }
        return this.driver;
    }

    @Override
    public BrowserConfig getConfig() {
        return builder.getConfig();
    }

    @Override
    public boolean isStarted() {
        return isActive();
    }

    @Override
    public Browser open(String url) {
        getDriver().get(url);
        return this;
    }

    @Override
    public Navigation navigate() {
        return getDriver().navigate();
    }

    @Override
    public Browser refresh() {
        getDriver().navigate().refresh();
        return this;
    }

    @Override
    public WindowManager windows() {
        return windows;
    }

    @Override
    public TabManager tabs() {
        return tabs;
    }

    @Override
    public DownloadManager downloads() {
        return downloads;
    }

    @Override
    public LogManager logs() {
        return logs;
    }

    @Override
    public BrowserActions actions() {
        return actions;
    }

    @Override
    public Activity disable() {
        close();
        return this;
    }

    @Override
    public void close() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        super.close();
    }
}
