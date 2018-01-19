package ru.mk.pump.web.page;

import java.util.concurrent.Callable;
import java.util.function.Predicate;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.web.page.api.PageLoader;

public class PageLoaderPump implements PageLoader {

    private final Page page;

    public PageLoaderPump(Page page) {
        this.page = page;
    }

    @Override
    public Page getPage() {
        return page;
    }

    @Override
    public void setChecker(Verifier checker) {

    }

    @Override
    public Verifier getChecker() {
        return null;
    }

    @Override
    public void addExistsElements(Element... elements) {

    }

    @Override
    public void addDisplayedElements(Element... elements) {

    }

    @Override
    public void addTextContainsElement(Element element, String text) {
        
    }

    @Override
    public void addPredicateElement(Element element, Predicate<Element> thisElementPredicate) {

    }

    @Override
    public void addAdditionalCondition(Callable<Boolean> booleanCallable) {

    }

    @Override
    public void checkElements() {

    }

    @Override
    public void checkAdditionalCondition() {

    }

    @Override
    public void checkUrl() {

    }
}