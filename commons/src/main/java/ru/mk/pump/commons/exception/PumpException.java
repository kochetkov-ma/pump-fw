package ru.mk.pump.commons.exception;

@SuppressWarnings({"WeakerAccess", "unused"})
public class PumpException extends AbstractPumpException {

    public PumpException(String message, String prefix) {
        super(new PumpMessage(message).withPre(prefix));
    }

    public PumpException(String message) {
        super(new PumpMessage(message));
    }

    public PumpException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public PumpException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public PumpException(String message, String prefix, Throwable cause) {
        super(new PumpMessage(message).withPre(prefix), cause);
    }

    public PumpException(String message, Throwable cause) {
        super(new PumpMessage(message), cause);
    }
}
