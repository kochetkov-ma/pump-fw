package ru.mk.pump.web.browsers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import ru.mk.pump.commons.activity.AbstractActivity;
import ru.mk.pump.commons.activity.Activity;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
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

    private final BrowserActions actions;

    private final LogManager logs;
    //endregion

    private WebDriver driver = null;

    //region INIT
    AbstractBrowser(@NotNull DriverBuilder builder, @NotNull UUID uuid) {
        super(uuid);
        this.builder = builder;
        this.config = builder.getConfig();
        this.id = this.config.getType().name() + "_" + uuid;
        this.downloads = new DownloadManager();
        this.actions = new BrowserActions(this::getDriver);
        this.logs = new LogManager();
        this.windows = new WindowManager(this);

    }
    //endregion

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Browser start() {
        if (isClosed()){
            throw new BrowserException("Cannot start browser. Browser has been already closed and this instance of Browser cannot start", this);
        }
        if (!isStarted()) {
            driver = builder.createAndStartDriver();
            if (driver == null) {
                throw new BrowserException("Cannot start browser. Incorrect driver builder", this);
            }
            activate();
        }
        return this;
    }

    @NotNull
    @Override
    public WebDriver getDriver() {
        if (driver == null) {
            throw new BrowserException("Cannot get WebDriver - browser was not started or closed", this);
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

        if (driver != null && !isClosed()) {
            if (isStarted()) {
                forkCloseByBrowserType();
                driver = null;
            } else {
                driver = null;
            }
        }
        super.close();
    }

    @Override
    public Map<String, String> getInfo() {
        final LinkedHashMap<String, String> result = new LinkedHashMap<>();
        result.put("id", id);
        result.put("builder", builder.getClass().getSimpleName());
        result.put("config", config.toString());
        return result;
    }

    private void forkCloseByBrowserType() {
        if (getConfig().getType() == BrowserType.PHANTOMJS) {
            driver.close();
            driver.quit();
        } else {
            driver.quit();
        }
    }
}
