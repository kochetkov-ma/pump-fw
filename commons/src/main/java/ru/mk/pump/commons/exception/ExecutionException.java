package ru.mk.pump.commons.exception;

@SuppressWarnings("unused")
public class ExecutionException extends PumpException {

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ExecutionException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }


}
