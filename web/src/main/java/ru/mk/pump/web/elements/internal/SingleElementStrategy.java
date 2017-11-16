package ru.mk.pump.web.elements.internal;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.mk.pump.web.exceptions.ElementFinderException;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;

@SuppressWarnings("WeakerAccess")
public class SingleElementStrategy extends FindStrategy {

    public SingleElementStrategy(InternalElement target) {
        super(target);
    }

    @Override
    public WebElement findSelf() {
        if (isRoot()) {
            return getFromRoot();
        } else {
            final WebElement parent = getTarget().getParent()
                .orElseThrow(() -> new ElementFinderException("Cannot get parent element", getTarget()))
                .getFinder().find();
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
            throw new ElementFinderNotFoundException("Find root element error", getTarget());
        }
    }

    protected WebElement getFromParent(WebElement parent) {
        try {
            final WebElement res = parent.findElement(getTarget().getBy());
            getTarget().getFinder().setCache(res);
            return res;
        } catch (WebDriverException ex) {
            getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            throw ex;
        }
    }

}
