package ru.mk.pump.web.page.api;

import java.util.concurrent.Callable;
import java.util.function.Predicate;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.ElementWaiter;

public interface PageLoader {

    Page getPage();

    ElementWaiter getWaiter();

    void setWaiter(ElementWaiter waiter);

    void setChecker(Verifier checker);

    Verifier getChecker();

    void addExistsElements(Element... elements);

    void addDisplayedElements(Element... elements);

    void addTextContainsElement(Element element, String text);

    void addPredicateElement(Element element, Predicate<Element> thisElementPredicate);

    void addAdditionalCondition(Callable<Boolean> booleanCallable);

    void checkElements();

    void checkAdditionalCondition();

    void checkUrl();
}
