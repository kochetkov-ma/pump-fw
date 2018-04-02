package ru.mk.pump.web.elements.internal;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;

@SuppressWarnings("WeakerAccess")
@Slf4j
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
            if (ConfigurationHolder.get().getElement().isFrameSupport()) {
                getTarget().getBrowser().actions().switchToRootFrame();
            }
            final WebElement res = getTarget().getBrowser().getDriver().findElement(getTarget().getBy());
            getTarget().getFinder().setCache(res);
            return res;
        } catch (WebDriverException ex) {
            getTarget().getFinder().setCache(null);
            onException();
            throw new ElementFinderNotFoundException("Find root element error", ex).withTargetElement(getTarget());
        }
    }

    protected void onException() {
        /*nothing*/
    }

    protected WebElement getFromParent(WebElement parent) {
        try {
            final WebElement res = parent.findElement(getTarget().getBy());
            getTarget().getFinder().setCache(res);
            return res;
        } catch (StaleElementReferenceException ex) {
            getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            onException();
            throw ex;
        } catch (NotFoundException ex) {
            if (ConfigurationHolder.get().getElement().isFrameSupport()) {
                getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            }
            onException();
            throw ex;
        } catch (WebDriverException ex) {
            getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            onException();
            throw ex;
        }
    }
}