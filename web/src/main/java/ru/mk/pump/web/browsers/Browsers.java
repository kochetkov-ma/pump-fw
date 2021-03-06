package ru.mk.pump.web.browsers;

import com.google.common.collect.Queues;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.History;
import ru.mk.pump.commons.utils.History.Info;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.browsers.builders.AndroidAppDriverBuilder;
import ru.mk.pump.web.browsers.builders.ChromeDriverBuilder;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.common.WebReporter;
import ru.mk.pump.web.exceptions.BrowserException;

import java.util.Deque;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
@Slf4j
public class Browsers implements AutoCloseable {

    private final ThreadLocal<Browser> currentBrowser;

    private final ThreadLocal<History<Browser>> browserHistory;

    private Set<Browser> internalAllBrowsers;

    public Browsers() {
        currentBrowser = new InheritableThreadLocal<>();
        internalAllBrowsers = new CopyOnWriteArraySet<>();
        browserHistory = InheritableThreadLocal.withInitial(() -> new History<>(100));
    }

    public Deque<Browser> getStartedBrowsers() {
        return Queues.newArrayDeque(getBrowsers().asList().stream()
                .map(Info::getPayload)
                .filter(Browser::isStarted)
                .collect(Collectors.toList()));
    }

    public Browser setBrowser(Browser browser) {
        checkClosed();
        internalAllBrowsers.add(browser);
        currentBrowser.set(browser);
        browserHistory.get().add(Info.of(browser.getId(), browser));
        /*update screen shooter*/
        WebReporter.init(browser);
        return browser;
    }

    public Browser newBrowser(BrowserConfig browserConfig) {
        checkClosed();
        final Browser newBrowser = new AbstractBrowser(Browsers.getBuilder(browserConfig)) {
        };
        setBrowser(newBrowser);
        return newBrowser;
    }

    /**
     * @return current browser or throw exception
     *
     * @throws BrowserException No active browser
     */
    public Browser get() {
        if (has()) {
            return currentBrowser.get();
        } else {
            throw new BrowserException("No one active browser in this thread", null);
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Browsers{");
        if (currentBrowser.get() != null) {
            sb.append("currentBrowser=").append(currentBrowser.get().getId());
        }
        if (browserHistory.get() != null) {
            sb.append("browserHistory=").append(Str.toString(browserHistory));
        }
        sb.append(", internalAllBrowsers=").append(internalAllBrowsers);
        sb.append('}');
        return sb.toString();
    }

    private static DriverBuilder getBuilder(BrowserConfig browserConfig) {
        switch (browserConfig.getType()) {
            case CHROME:
                return new ChromeDriverBuilder(browserConfig);
            case ANDROID_APP:
                return new AndroidAppDriverBuilder(browserConfig);
            case FIREFOX:
                throw new UnsupportedOperationException();
            case IE:
                throw new UnsupportedOperationException();
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void checkClosed() {
        if (isClosed()) {
            throw new IllegalStateException("The Object was already closed. Or never had activated");
        }
    }
}
