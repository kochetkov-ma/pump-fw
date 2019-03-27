package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.Finder;

import javax.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor
public class ElementFinderException extends ElementException {
    public ElementFinderException(@Nullable String title) {
        this(title, null);
    }

    public ElementFinderException(@Nullable String title, @Nullable Throwable cause) {
        super(title, null);
    }

    @Override
    public ElementFinderException withElement(@Nullable Element element) {
        return (ElementFinderException) super.withElement(element);
    }

    @Override
    public ElementFinderException withFinder(@Nullable Finder finder) {
        return (ElementFinderException) super.withFinder(finder);
    }
}
