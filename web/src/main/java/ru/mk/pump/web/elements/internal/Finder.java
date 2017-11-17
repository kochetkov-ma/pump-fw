package ru.mk.pump.web.elements.internal;

import java.util.Optional;
import lombok.Getter;
import org.hamcrest.Matchers;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.exceptions.ElementFinderException;

@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
class Finder {

    @Getter
    private final InternalElement mainElement;

    private WebElement cache = null;

    private FindStrategy DEFAULT_FIND_STRATEGY;

    private FindStrategy findStrategy;

    public Finder(InternalElement mainElement) {

        this.mainElement = mainElement;
        this.findStrategy = new SingleElementStrategy(getMainElement());
    }

    Finder setFindStrategy(FindStrategy findStrategy) {
        this.findStrategy = findStrategy;
        return this;
    }

    WebElement get() {
        if (!getCache().isPresent()) {
            return findStrategy.findSelf();
        } else {
            return cache;
        }
    }

    WaitResult<WebElement> find() {
        setCache(null);
        return new Waiter()
            .withNotIgnoreExceptions(ElementFinderException.class)
            .withNotIgnoreExceptions(NoSuchElementException.class)
            .waitIgnoreExceptions(5, 0, this::get, Matchers.notNullValue(WebElement.class));
    }

    Optional<WebElement> getCache() {
        return Optional.ofNullable(cache);
    }

    Finder setCache(WebElement targetElement) {
        this.cache = targetElement;
        return this;
    }
}
