package ru.mk.pump.web.browsers;

import lombok.NonNull;
import lombok.ToString;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import ru.mk.pump.commons.reporter.Attachment;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.browsers.api.BrowserListener;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.common.WebReporter;
import ru.mk.pump.web.common.api.WebListenersConfiguration;
import ru.mk.pump.web.exceptions.BrowserException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static ru.mk.pump.web.common.WebReporter.getReporter;

/**
 * Created by kochetkov-ma on 5/31/17.
 */
@SuppressWarnings("WeakerAccess")
@ToString(of = {"id", "config"}, callSuper = true)
public abstract class AbstractBrowser extends BrowserNotifier implements Browser {

    //region FINALS
    private final DriverBuilder builder;

    private final BrowserConfig config;

    private final TabManager tabManager;

    private final DownloadManager downloads;

    private final BrowserActions actions;

    private final LogManager logs;
    private final String id;
    //endregion

    private WebDriver driver = null;
    private boolean started = false;
    private boolean closed = false;

    //region INIT
    public AbstractBrowser(@NonNull DriverBuilder builder) {

        this(builder, builder.getConfig().getType().name() + "_" + UUID.randomUUID().toString());

    }

    public AbstractBrowser(@NonNull DriverBuilder builder, @NonNull String uuid) {

        this.builder = builder;
        this.config = builder.getConfig();
        this.id = uuid;
        this.downloads = new DownloadManager(getConfig());
        this.actions = new BrowserActions(this::getDriver);
        this.logs = new LogManager();
        this.tabManager = new TabManager(this);
        afterConstruct();
    }
    //endregion

    protected void afterConstruct() {

        addListener(newDefaultListener());
        final WebListenersConfiguration configuration = WebReporter.getListenersConfiguration();
        if (configuration != null) {
            if (configuration.eraseBrowserListener()) {
                clearListeners();
            }
            addListeners(configuration.getBrowserListener(this));
        }
    }

    @Override
    public String getId() {

        return id;
    }

    @Override
    public Browser start() {

        notifyOnOpen(this);
        if (isClosed()) {
            throw new BrowserException("Cannot start browser. Browser has been already closed and this instance of Browser cannot start", this);
        }
        if (!isStarted()) {
            driver = builder.createAndStartDriver();
            if (driver == null) {
                throw new BrowserException("Cannot start browser. Incorrect driver builder", this);
            }
        }
        tabManager.refresh();
        started = true;
        closed = false;
        return this;
    }

    @Override
    public boolean isClosed() {

        return closed;
    }

    public Browser setSize(Dimension dimension) {

        getDriver().manage().window().setSize(dimension);
        return this;
    }

    public Browser maximize() {

        getDriver().manage().window().maximize();
        return this;
    }

    @NonNull
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

        return started;
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
    public TabManager tabs() {

        return tabManager;
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
    public void close() {

        notifyOnClose(this, null);
        if (driver != null && !isClosed()) {
            if (isStarted()) {
                driver.quit();
                driver = null;
            } else {
                driver = null;
            }
        }
        started = false;
        closed = true;
    }

    @Override
    public Map<String, String> getInfo() {

        final LinkedHashMap<String, String> result = new LinkedHashMap<>();
        result.put("id", id);
        result.put("builder", builder.getClass().getSimpleName());
        result.put("config", config.toString());
        return result;
    }

    protected BrowserListener newDefaultListener() {

        return new BrowserListener() {
            private final Attachment empty = getReporter().attachments().dummy();

            @Override
            public void onOpen(Browser browser) {

                getReporter().info("Browser was opened", browser.toPrettyString(), empty);
            }

            @Override
            public void onClose(Browser browser, Throwable fromArgsOrNull) {

                if (fromArgsOrNull != null) {
                    getReporter().warn("Browser was closed with problems", browser.toPrettyString(), fromArgsOrNull);
                } else {
                    getReporter().info("Browser was closed", browser.toPrettyString(), empty);
                }
            }

            @Override
            public void onOpenTab(Browser browser, String tabUuid) {

                getReporter().info(format("Tab '%s' was opened", tabUuid), browser.toPrettyString(), empty);
            }

            @Override
            public void onCloseTab(Browser browser, String tabUuid) {

                getReporter().info(format("Tab '%s' was closed", tabUuid), browser.toPrettyString(), empty);
            }
        };
    }
}
