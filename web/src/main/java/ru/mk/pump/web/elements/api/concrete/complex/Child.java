package ru.mk.pump.web.elements.api.concrete.complex;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import ru.mk.pump.commons.utils.ParameterUtils;
import ru.mk.pump.commons.utils.Pre;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.BaseElement;

import javax.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString(exclude = {"parentElement", "cache"})
@Slf4j
public class Child<T extends Element> {

    private final BaseElement parentElement;

    @Getter
    private final String parameterName;

    private By[] defaultByArray;

    private T cache;


    public Child(@NonNull BaseElement parentElement, @Nullable String parameterName) {
        Pre.checkStringNotBlank(parameterName);
        this.parameterName = parameterName;
        this.parentElement = parentElement;
    }

    public Child(@NonNull BaseElement parentElement, @NonNull By[] childByArray) {
        this.parameterName = null;
        this.parentElement = parentElement;
        withDefaultBy(childByArray);
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
     *
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
        defaultByArray = ParameterUtils.getOrDefault(parentElement.getParams(), parameterName, By[].class, defaultByArray);
        return defaultByArray;
    }
}