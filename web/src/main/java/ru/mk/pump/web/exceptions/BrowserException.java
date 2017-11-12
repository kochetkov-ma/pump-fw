package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.ThrowableMessage;

@SuppressWarnings("unused")
public class BrowserException extends PumpException {

    public BrowserException(String message) {
        super(message);
    }

    public BrowserException(ThrowableMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public BrowserException(ThrowableMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public BrowserException(String message, Throwable cause) {
        super(message, cause);
    }
}
