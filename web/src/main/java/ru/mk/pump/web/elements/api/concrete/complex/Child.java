package ru.mk.pump.web.elements.api.concrete.complex;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import ru.mk.pump.commons.utils.Preconditions;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.utils.Parameters;

@SuppressWarnings("WeakerAccess")
@ToString(exclude = {"parentElement", "cache"})
@Slf4j
public class Child<T extends Element> {

    private final BaseElement parentElement;

    @Getter
    private final String parameterName;

    private By[] defaultByArray;

    private T cache;

    public Child(@NotNull String parameterName, @NotNull BaseElement parentElement) {
        Preconditions.checkStringNotBlank(parameterName);
        this.parameterName = parameterName;
        this.parentElement = parentElement;
    }

    /**
     * Set default locator. If parent element has special locator it will be rewrite
     */
    public Child<T> withDefaultBy(@Nullable By[] defaultByArray) {
        this.defaultByArray = defaultByArray;
        return this;
    }

    /**
     * Find child element and write to local cache
     * @param childClass Child class element to create if any element has been found
     * @return new child element
     */
    public T find(Class<T> childClass) {
        log.trace("Child.find call : {}", childClass);
        cache = parentElement.getSubElements(childClass).find(extractBys());
        //TODO::Удалить?
        /* Безконтрольное использование параметров, которые предназначены только для главного элемента
        ((BaseElement) cache).withParams(parentElement.getParams());
        */
        log.trace("Child.find return : {}", cache.getClass());
        return cache;
    }

    /**
     * If cache is not null return child element from cache. Else call {@link #find(Class)}
     *
     * @param childClass Child class element to create if any element has been found
     * @return new child element
     */
    public T get(Class<T> childClass) {
        log.trace("Child.get call : {}", childClass);
        if (cache == null) {
            log.trace("Child.get return : call #find");
            return find(childClass);
        }
        log.trace("Child.get return from cache : {}", cache.getClass());
        return cache;
    }

    /**
     * If child element is optional. Check is child element has any locators for search.
     * Use this check before any actions with child element
     */
    public boolean isDefined() {
        return ArrayUtils.isNotEmpty(extractBys());
    }

    private By[] extractBys() {
        defaultByArray = Parameters.getOrDefault(parentElement.getParams(), parameterName, By[].class, defaultByArray);
        return defaultByArray;
    }
}