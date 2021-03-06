package ru.mk.pump.web.elements.internal;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementFinderException;
import ru.mk.pump.web.exceptions.ElementNotFoundException;

import java.util.List;

@Slf4j
@SuppressWarnings("WeakerAccess")
class ListElementStrategy extends FindStrategy {

    public ListElementStrategy(InternalElement target) {
        super(target);
    }

    @Override
    public WebElement findSelf() {
        if (!getTarget().isList()) {
            throw new ElementFinderException()
                    .withInternalElement(getTarget())
                    .withTitle(Str.format("Selected find strategy '{}' don't work with no list rules", getClass().getSimpleName()));
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
            throw new ElementNotFoundException().withInternalElement(getTarget()).withTitle("Find root element error").withCause(ex);
        }
    }

    protected List<WebElement> getList() {
        try {
            log.debug("Try to findElements for {}", getTarget());
            final List<WebElement> webElements = getTarget().getParent()
                    .orElseThrow(() -> new ElementNotFoundException()
                            .withInternalElement(getTarget())
                            .withTitle("Cannot find parent element"))
                    .getFinder().get().findElements(getTarget().getBy());
            log.debug("Result getList() : " + Str.toPrettyString(webElements));
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
            throw new ElementNotFoundException()
                    .withInternalElement(getTarget())
                    .withTitle(Str.format("Expected element index '{}' is exceeds the list size '{}'", getTarget().getIndex(), webElements.size()))
                    .withExtra("finder strategy", this);
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
