package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

import javax.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor
public class ElementException extends WebException {

    public ElementException(@Nullable String title, @Nullable Element element) {
        this(title, element, null);
    }

    public ElementException(@Nullable String title, @Nullable Element element, @Nullable Throwable cause) {
        super(title, cause);
        withElement(element);
    }

    @Override
    public ElementException withElement(@Nullable Element element) {
        return (ElementException) super.withElement(element);
    }

    @Override
    public ElementException withParent(@Nullable Element parent) {
        return (ElementException) super.withParent(parent);
    }

    public ElementStateException withInternalElement(@Nullable InternalElement internalElement) {
        return (ElementStateException) super.withExtra("element", internalElement);
    }
}