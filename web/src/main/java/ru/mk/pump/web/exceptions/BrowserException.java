package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.PumpMessage;

@SuppressWarnings("unused")
public class BrowserException extends PumpException {

    public BrowserException(String message) {
        super(message);
    }

    public BrowserException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public BrowserException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public BrowserException(String message, Throwable cause) {
        super(message, cause);
    }
}
