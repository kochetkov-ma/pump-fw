package ru.mk.pump.commons.exception;

@SuppressWarnings({"WeakerAccess", "unused"})
public class PumpException extends AbstractPumpException {

    public PumpException(String message, String prefix) {
        super(new ThrowableMessage(message).withPre(prefix));
    }

    public PumpException(String message) {
        super(new ThrowableMessage(message));
    }

    public PumpException(ThrowableMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public PumpException(ThrowableMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public PumpException(String message, String prefix, Throwable cause) {
        super(new ThrowableMessage(message).withPre(prefix), cause);
    }

    public PumpException(String message, Throwable cause) {
        super(new ThrowableMessage(message), cause);
    }
}
