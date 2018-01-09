package ru.mk.pump.web.elements.api.concrete.complex;

import lombok.Getter;
import lombok.ToString;
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

    public Child<T> withDefaultBy(@Nullable By[] defaultByArray) {
        this.defaultByArray = defaultByArray;
        return this;
    }

    public T find(Class<T> childClass) {
        cache = parentElement.getSubElements(childClass).find(extractBys());
        ((BaseElement)cache).withParams(parentElement.getParams());
        return cache;
    }

    public T get(Class<T> childClass) {
        if (cache == null) {
            return find(childClass);
        }
        return cache;
    }

    public boolean isDefined() {
        return ArrayUtils.isNotEmpty(extractBys());
    }

    private By[] extractBys() {
        defaultByArray = Parameters.getOrDefault(parentElement.getParams(), parameterName, By[].class, defaultByArray);
        return defaultByArray;
    }
}