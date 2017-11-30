package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.internal.Finder;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ElementFinderException extends ElementException {

    static final String FINDER = "finder";

    public ElementFinderException(String title) {
        super(title);
    }

    public ElementFinderException(String title, Throwable cause) {
        super(title, cause);
    }

    public ElementFinderException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ElementFinderException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public ElementException withTarget(Finder finder) {
        addTarget(FINDER, finder);
        withElement(finder.getMainElement());
        return this;
    }
}
