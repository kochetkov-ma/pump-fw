package ru.mk.pump.commons.exception;

@SuppressWarnings("unused")
public class UtilException extends PumpException {

    private static final String PRE = "Pump utility exception";

    public UtilException(String message) {
        super(message, PRE);
    }

    public UtilException(ThrowableMessage exceptionMessage) {
        super(exceptionMessage.withPre(PRE));
    }

    public UtilException(ThrowableMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage.withPre(PRE), cause);
    }

    public UtilException(String message, Throwable cause) {
        super(message, PRE, cause);
    }
}
