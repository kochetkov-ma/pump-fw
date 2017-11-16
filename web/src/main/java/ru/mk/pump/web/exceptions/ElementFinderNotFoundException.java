package ru.mk.pump.web.exceptions;

import ru.mk.pump.web.elements.internal.InternalElement;

@SuppressWarnings("unused")
public class ElementFinderNotFoundException extends ElementFinderException {

    public ElementFinderNotFoundException(String message) {
        super(message);
    }

    public ElementFinderNotFoundException(InternalElement element) {
        super(element);
    }

    public ElementFinderNotFoundException(InternalElement element, Throwable throwable) {
        super(element, throwable);
    }

    public ElementFinderNotFoundException(String message, InternalElement element) {
        super(message, element);
    }

    public ElementFinderNotFoundException(String message, InternalElement element, Throwable throwable) {
        super(message, element, throwable);
    }
}
