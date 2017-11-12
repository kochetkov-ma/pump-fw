package ru.mk.pump.commons.exception;

@SuppressWarnings("unused")
public class TimeoutException extends PumpException {

    public TimeoutException(ThrowableMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public TimeoutException(ThrowableMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
