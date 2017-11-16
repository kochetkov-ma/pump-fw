package ru.mk.pump.web.elements.internal;

import java.util.Optional;
import lombok.Getter;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.exceptions.ElementFinderException;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;

@SuppressWarnings({"UnusedReturnValue", "unused"})
class Finder {

    @Getter
    private final InternalElement mainElement;

    private WebElement cache = null;

    private FindStrategy DEFAULT_FIND_STRATEGY = new SingleElementStrategy(getMainElement());

    private FindStrategy findStrategy = DEFAULT_FIND_STRATEGY;

    public Finder(InternalElement mainElement) {
        this.mainElement = mainElement;
    }

    Finder setFindStrategy(FindStrategy findStrategy) {
        this.findStrategy = findStrategy;
        return this;
    }

    WebElement find() {
        if (!getCache().isPresent()) {
            return findStrategy.findSelf();
        } else {
            return cache;
        }
    }

    WaitResult<WebElement> get() {
        setCache(null);
        return new Waiter()
            .withNotIgnoreExceptions(ElementFinderException.class)
            .waitIgnoreExceptions(5, 0, this::find, Matchers.notNullValue(WebElement.class));
    }

    Optional<WebElement> getCache() {
        return Optional.ofNullable(cache);
    }

    Finder setCache(WebElement targetElement) {
        this.cache = targetElement;
        return this;
    }
}
