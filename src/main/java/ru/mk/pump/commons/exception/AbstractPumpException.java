package ru.mk.pump.commons.exception;

@SuppressWarnings("WeakerAccess")
abstract class AbstractPumpException extends RuntimeException {

    public AbstractPumpException(ThrowableMessage exceptionMessage) {
        super(exceptionMessage.toPrettyString());
    }

    public AbstractPumpException(ThrowableMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage.toPrettyString(), cause);
    }


}
