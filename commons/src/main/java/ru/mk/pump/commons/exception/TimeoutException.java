package ru.mk.pump.commons.exception;

@SuppressWarnings("unused")
public class TimeoutException extends PumpException {

    public TimeoutException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public TimeoutException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
