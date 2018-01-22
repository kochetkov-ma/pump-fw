package ru.mk.pump.web.page;

import static java.lang.String.format;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.Setter;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.ElementWaiter;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.web.page.api.PageLoader;

@SuppressWarnings("WeakerAccess")
public class PageLoaderPump implements PageLoader {

    private final Page page;

    @Setter
    private ElementWaiter waiter;

    @Setter
    @Getter
    private Verifier checker;

    private Set<Element> existsElements = Sets.newHashSet();

    private Set<Element> displayedElements = Sets.newHashSet();

    private Map<Element, String> textContainsElements = Maps.newHashMap();

    private Map<Element, Predicate<Element>> predicateElements = Maps.newHashMap();

    private Set<Callable<Boolean>> conditions = Sets.newHashSet();

    public PageLoaderPump(Page page, Verifier checker) {
        this.page = page;
        this.checker = checker;
    }

    @Override
    public Page getPage() {
        return page;
    }

    @Override
    public ElementWaiter getWaiter() {
        if (waiter == null) {
            waiter = ElementWaiter.newWaiterS(10);
        }
        return waiter;
    }

    @Override
    public void addExistsElements(Element... elements) {
        existsElements.addAll(Arrays.asList(elements));
    }

    @Override
    public void addDisplayedElements(Element... elements) {
        displayedElements.addAll(Arrays.asList(elements));

    }

    @Override
    public void addTextContainsElement(Element element, String text) {
        textContainsElements.put(element, text);
    }

    @Override
    public void addPredicateElement(Element element, Predicate<Element> thisElementPredicate) {
        predicateElements.put(element, thisElementPredicate);
    }

    @Override
    public void addAdditionalCondition(Callable<Boolean> booleanCallable) {
        conditions.add(booleanCallable);
    }

    @Override
    public void checkElements() {
        existsElements.forEach(el -> checker.checkTrue(format("On page '%s' element '%s' is exists", getPage().getName(), el.info().getName()), el.isExists()));
        displayedElements.forEach(el -> checker.checkTrue(format("On page '%s' element '%s' is displayed",getPage().getName() , el.info().getName()), el.isDisplayed()));
        textContainsElements.forEach((el, text) -> checker.contains(format("On page '%s' element '%s' contains text",getPage().getName(), el.info().getName()), text, el.getText()));
        predicateElements.forEach((el, predicate) -> checker.checkTrue(format("On page '%s' predicate element '%s' is success", getPage().getName(), el.info().getName()), predicate.test(el)));
    }

    @Override
    public void checkAdditionalCondition() {
        conditions.forEach(call -> checker.noExceptions("Custom condition with wait", () -> getWaiter().wait(call).throwExceptionOnFail()));
    }

    @Override
    public void checkUrl() {
        checker.contains("Page URL contains text", getPage().getUrl(), getPage().getBrowser().actions().getCurrentUrl());
    }
}