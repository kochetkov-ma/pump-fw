package ru.mk.pump.web.elements.internal;

import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementFinderException;

@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
@Slf4j
@ToString(of = {"findStrategy", "logReturnCacheAmount", "logFindSelfAmount", "logFindAmount"})
public class Finder implements StrictInfo {

    public static final int FIND_TIMEOUT_S = 1;

    @Getter
    private final InternalElement mainElement;

    private long logReturnCacheAmount = 0;

    private long logFindSelfAmount = 0;

    private long logFindAmount = 0;

    private WaitResult<WebElement> lastResult;

    private WebElement cache = null;

    private FindStrategy DEFAULT_FIND_STRATEGY;

    private FindStrategy findStrategy;

    Finder(InternalElement mainElement) {

        this.mainElement = mainElement;
        this.findStrategy = new SingleElementStrategy(getMainElement());
    }

    public Finder setFindStrategy(FindStrategy findStrategy) {
        this.findStrategy = findStrategy;
        return this;
    }

    Optional<WaitResult<WebElement>> getLast() {
        return Optional.ofNullable(lastResult);
    }

    WebElement get() {
        if (!getCache().isPresent()) {
            logFindSelfAmount++;
            log.debug("Call findSelf() from Finder.get() '{}'", toString());
            return findStrategy.findSelf();
        } else {
            logReturnCacheAmount++;
            log.debug("Get cache from Finder.get() '{}'", cache);
            return cache;
        }
    }

    public WaitResult<WebElement> findFast() {
        logFindAmount++;
        log.debug("Call Finder.findFast() '{}'" + toString());
        try {
            final WebElement result = get();
            if (result != null) {
                lastResult = new WaitResult<WebElement>(true, 0).withResult(result);
            } else {
                lastResult = new WaitResult<>(false, 0);
            }
        } catch (ElementFinderException | NotFoundException ex) {
            lastResult = new WaitResult<WebElement>(false, 0).withCause(ex);
        }
        return lastResult;
    }

    public WaitResult<WebElement> find() {
        logFindAmount++;
        log.debug("Call Finder.find() '{}'" + toString());
        setCache(null);
        lastResult = new Waiter()
            .withNotIgnoreExceptions(ElementFinderException.class)
            .withNotIgnoreExceptions(NotFoundException.class)
            .waitIgnoreExceptions(FIND_TIMEOUT_S, 0, this::get, Matchers.notNullValue(WebElement.class));
        return lastResult;
    }

    public Finder clearCache() {
        this.cache = null;
        return this;
    }

    Optional<WebElement> getCache() {
        return Optional.ofNullable(cache);
    }

    Finder setCache(WebElement targetElement) {
        this.cache = targetElement;
        return this;
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoBuilder("Finder")
            .put("main element", mainElement.toString())
            .put("cached result", lastResult.toString())
            .put("find strategy", findStrategy.getClass().getSimpleName())
            .put("logFindAmount", Strings.toString(logFindAmount))
            .put("logFindSelfAmount", Strings.toString(logFindSelfAmount))
            .put("logReturnCacheAmount", Strings.toString(logReturnCacheAmount))
            .build();
    }
}
