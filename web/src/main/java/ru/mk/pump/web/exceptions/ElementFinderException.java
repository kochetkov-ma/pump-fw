package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.ThrowableMessage;
import ru.mk.pump.web.elements.internal.InternalElement;

@SuppressWarnings("unused")
public class ElementFinderException extends ElementException {

    public ElementFinderException(String message) {
        super(message);
    }

    public ElementFinderException(InternalElement element) {
        super(element);
    }

    public ElementFinderException(InternalElement element, Throwable throwable) {
        super(element, throwable);
    }

    public ElementFinderException(String message, InternalElement element) {
        super(message, element);
    }

    public ElementFinderException(String message, InternalElement element, Throwable throwable) {
        super(message, element, throwable);
    }
}
