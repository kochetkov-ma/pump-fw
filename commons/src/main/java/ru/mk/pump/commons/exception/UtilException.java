package ru.mk.pump.commons.exception;

@SuppressWarnings("unused")
public class UtilException extends PumpException {

    public UtilException(String title) {
        super(title);
    }

    public UtilException(String title, Throwable cause) {
        super(title, cause);
    }

    public UtilException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public UtilException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
