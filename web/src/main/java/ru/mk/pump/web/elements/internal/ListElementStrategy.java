package ru.mk.pump.web.elements.internal;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementFinderException;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;

@SuppressWarnings("WeakerAccess")
class ListElementStrategy extends FindStrategy {


    public ListElementStrategy(InternalElement target) {
        super(target);
    }

    @Override
    public WebElement findSelf() {
        if (!getTarget().isList()) {
            throw new ElementFinderException(String.format("Selected find strategy '%s' don't work with no list elements", getClass().getSimpleName()), getTarget());
        }
        if (isRoot()) {
            return getFromRoot();

        } else {
            return getFromList(getList());
        }
    }

    protected WebElement getFromRoot() {
        try {
            final List<WebElement> webElements = getTarget().getBrowser().getDriver().findElements(getTarget().getBy());
            final WebElement res = getFromList(webElements);
            getTarget().getFinder().setCache(res);
            return res;
        } catch (WebDriverException ex) {
            getTarget().getFinder().setCache(null);
            throw new ElementFinderNotFoundException("Find root element error", getTarget(), ex);
        }
    }

    protected List<WebElement> getList(){
        try {
            return getTarget().getParent()
                    .orElseThrow(() -> new ElementFinderException("Cannot find parent element", getTarget()))
                    .getFinder().get().findElements(getTarget().getBy());
        } catch (StaleElementReferenceException ex){
            getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            throw ex;
        } catch (NoSuchElementException ex) {
            throw ex;
        } catch (WebDriverException ex) {
            getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            throw ex;
        }
    }

    protected WebElement getFromList(List<WebElement> webElements) {
        /*Когда список элементов изменился нужно пересчитывать все элементы*/
        if (webElements.size() <= getTarget().getIndex()) {
            throw new ElementFinderNotFoundException(String
                .format("Expected element index '%s' is exceeds the list size '%s'", getTarget().getIndex(), webElements.size()), getTarget());
        }
        try {
            final WebElement res = webElements.get(getTarget().getIndex());
            getTarget().getFinder().setCache(res);
            return res;
        } catch (WebDriverException ex) {
            getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            throw ex;
        }
    }
}
