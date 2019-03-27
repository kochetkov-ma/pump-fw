package ru.mk.pump.commons.exception;

@SuppressWarnings("unused")
public class ExecutionException extends PumpException {

    public ExecutionException(String title) {
        super(title);
    }

    public ExecutionException(String title, Throwable cause) {
        super(title, cause);
    }

    public ExecutionException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}