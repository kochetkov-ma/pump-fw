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

    @Override
    protected Throwable checkCauseAndReorganize(Throwable cause) {
        if (cause instanceof PumpException) {
            final PumpException exception = (PumpException) cause;
            getEnv().entrySet().removeIf((entry) -> exception.getEnv().containsKey(entry.getKey()));
        }
        return cause;
    }
}
