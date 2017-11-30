package ru.mk.pump.commons.exception;

@SuppressWarnings({"WeakerAccess", "unused"})
public class PumpException extends AbstractPumpException {

    public PumpException(String title) {
        super(new PumpMessage(title));
    }

    public PumpException(String title, Throwable cause) {
        super(new PumpMessage(title), cause);
    }

    public PumpException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public PumpException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
