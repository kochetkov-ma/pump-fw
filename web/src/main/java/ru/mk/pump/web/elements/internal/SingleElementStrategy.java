package ru.mk.pump.web.elements.internal;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;

@SuppressWarnings("WeakerAccess")
class SingleElementStrategy extends FindStrategy {

    public SingleElementStrategy(InternalElement target) {
        super(target);
    }

    @Override
    public WebElement findSelf() {
        if (isRoot()) {
            return getFromRoot();
        } else {
            final WebElement parent = getTarget().getParent()
                .orElseThrow(() -> new ElementFinderNotFoundException("Cannot find parent element").withTargetElement(getTarget()))
                .getFinder().get();
            return getFromParent(parent);
        }
    }

    protected WebElement getFromRoot() {
        try {
            final WebElement res = getTarget().getBrowser().getDriver().findElement(getTarget().getBy());
            getTarget().getFinder().setCache(res);
            return res;
        } catch (WebDriverException ex) {
            getTarget().getFinder().setCache(null);
            throw new ElementFinderNotFoundException("Find root element error", ex).withTargetElement(getTarget());
        }
    }

    protected WebElement getFromParent(WebElement parent) {
        try {
            final WebElement res = parent.findElement(getTarget().getBy());
            getTarget().getFinder().setCache(res);
            return res;
        } catch (StaleElementReferenceException ex) {
            getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            throw ex;
        } catch (NoSuchElementException ex) {
            throw ex;
        } catch (WebDriverException ex) {
            getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            throw ex;
        }
    }

}
