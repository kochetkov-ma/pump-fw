package ru.mk.pump.commons.exception;

public class UtilException extends PumpException {

    private static final String PRE = "Pump utility exception";

    public UtilException(String message) {
        super(new ExceptionMessage(message).withPre(PRE));
    }

    public UtilException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.withPre(PRE));
    }

    public UtilException(ExceptionMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage.withPre(PRE), cause);
    }

    public UtilException(String message, Throwable cause) {
        super(new ExceptionMessage(message).withPre(PRE), cause);
    }
}
