package ru.mk.pump.web.browsers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.History;
import ru.mk.pump.commons.utils.History.Info;
import ru.mk.pump.web.browsers.builders.ChromeDriverBuilder;
import ru.mk.pump.web.browsers.builders.GhostDriverBuilder;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.exceptions.BrowserException;

@SuppressWarnings({"WeakerAccess", "unused"})
@Slf4j
public class Browsers implements AutoCloseable {

    private final ThreadLocal<Browser> currentBrowser;

    private final ThreadLocal<History<Browser>> browserHistory;

    private List<Browser> internalAllBrowsers;

    public Browsers() {
        currentBrowser = new InheritableThreadLocal<>();
        internalAllBrowsers = new CopyOnWriteArrayList<>();
        browserHistory = InheritableThreadLocal.withInitial(() -> new History<>(100));
    }

    private static DriverBuilder getBuilder(BrowserConfig browserConfig) {
        switch (browserConfig.getType()) {
            case CHROME:
                return new ChromeDriverBuilder(browserConfig);
            case PHANTOMJS:
                return new GhostDriverBuilder(browserConfig);
            case FIREFOX:
                throw new UnsupportedOperationException();
            case IE:
                throw new UnsupportedOperationException();
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Browser addBrowser(Browser browser){
        checkClosed();
        internalAllBrowsers.add(browser);
        currentBrowser.set(browser);
        browserHistory.get().add(Info.of(browser.getId(), browser));
        return currentBrowser.get();
    }

    public Browser newBrowser(BrowserConfig browserConfig) {
        checkClosed();
        final Browser newBrowser = new AbstractBrowser(Browsers.getBuilder(browserConfig), UUID.randomUUID().toString()) {
        };
        internalAllBrowsers.add(newBrowser);
        currentBrowser.set(newBrowser);
        browserHistory.get().add(Info.of(newBrowser.getId(), newBrowser));
        return currentBrowser.get();
    }

    /**
     *
     * @throws BrowserException No active browser
     * @return current browser or throw exception
     */
    public Browser get() {
        if (has()) {
            return currentBrowser.get();
        } else {
            throw new BrowserException("No one active browser in this thread");
        }
    }

    public boolean has() {
        checkClosed();
        return currentBrowser.get() != null;
    }

    public History<Browser> getBrowsers() {
        return browserHistory.get();
    }

    @Override
    public void close() {
        if (!isClosed()) {
            internalAllBrowsers.forEach(Browser::close);
            internalAllBrowsers = null;
        }
    }

    public boolean isClosed() {
        return internalAllBrowsers == null;
    }

    public void closeCurrentThread() {
        if (!isClosed()) {
            getBrowsers().getAll().forEach(browser -> browser.getPayload().close());
            currentBrowser.remove();
        }
    }

    private void checkClosed() {
        if (isClosed()) {
            throw new IllegalStateException("The Object was already closed");
        }
    }
}
