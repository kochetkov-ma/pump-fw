package ru.mk.pump.web.elements.internal;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementFinderException;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;

@Slf4j
@SuppressWarnings("WeakerAccess")
class ListElementStrategy extends FindStrategy {


    public ListElementStrategy(InternalElement target) {
        super(target);
    }

    @Override
    public WebElement findSelf() {
        if (!getTarget().isList()) {
            throw new ElementFinderException(String.format("Selected find strategy '%s' don't work with no list rules", getClass().getSimpleName()))
                .withTargetElement(getTarget());
        }
        if (isRoot()) {
            return getFromRoot();

        } else {
            return getFromList(getList());
        }
    }

    protected WebElement getFromRoot() {
        try {
            if (ConfigurationHolder.get().getElement().isFrameSupport()) {
                getTarget().getBrowser().actions().switchToRootFrame();
            }
            final List<WebElement> webElements = getTarget().getBrowser().getDriver().findElements(getTarget().getBy());
            final WebElement res = getFromList(webElements);
            getTarget().getFinder().setCache(res);
            return res;
        } catch (WebDriverException ex) {
            getTarget().getFinder().setCache(null);
            throw new ElementFinderNotFoundException("Find root element error", ex).withTargetElement(getTarget());
        }
    }

    protected List<WebElement> getList() {
        try {
            log.debug("Try to findElements for {}", getTarget());
            final List<WebElement> webElements = getTarget().getParent()
                .orElseThrow(() -> new ElementFinderNotFoundException("Cannot find parent element")
                    .withTargetElement(getTarget()))
                .getFinder().get().findElements(getTarget().getBy());
            log.debug("Result getList() : " + Strings.toPrettyString(webElements));
            return webElements;
        } catch (StaleElementReferenceException ex) {
            getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            throw ex;
        } catch (NotFoundException ex) {
            if (ConfigurationHolder.get().getElement().isFrameSupport()) {
                getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            }
            throw ex;
        } catch (WebDriverException ex) {
            getTarget().getParent().ifPresent(p -> p.getFinder().setCache(null));
            throw ex;
        }
    }

    protected WebElement getFromList(List<WebElement> webElements) {
        /*Когда список элементов изменился нужно пересчитывать все элементы*/
        if (webElements.size() <= getTarget().getIndex()) {
            throw new ElementFinderNotFoundException(
                String.format("Expected element index '%s' is exceeds the list size '%s'", getTarget().getIndex(), webElements.size()))
                .withTargetElement(getTarget())
                .withTargetInfo("finder strategy", this);
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
