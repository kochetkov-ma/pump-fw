package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpMessage;

@SuppressWarnings("unused")
public class ElementFinderNotFoundException extends ElementFinderException {

    public ElementFinderNotFoundException(String title) {
        super(title);
    }

    public ElementFinderNotFoundException(String title, Throwable cause) {
        super(title, cause);
    }

    public ElementFinderNotFoundException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ElementFinderNotFoundException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
