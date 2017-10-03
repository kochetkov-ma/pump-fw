package ru.mk.pump.commons.exception;

@SuppressWarnings("WeakerAccess")
abstract class PumpException extends RuntimeException {

    public PumpException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.toPrettyString());
    }

    public PumpException(ExceptionMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage.toPrettyString(), cause);
    }


}
