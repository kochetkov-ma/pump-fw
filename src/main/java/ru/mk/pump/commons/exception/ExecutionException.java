package ru.mk.pump.commons.exception;

public class ExecutionException extends PumpException {

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(ThrowableMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ExecutionException(ThrowableMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }


}
