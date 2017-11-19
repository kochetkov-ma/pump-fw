package ru.mk.pump.commons.exception;

@SuppressWarnings("WeakerAccess")
abstract class AbstractPumpException extends RuntimeException {

    public AbstractPumpException(PumpMessage exceptionMessage) {
        super(exceptionMessage.toPrettyString());
    }

    public AbstractPumpException(PumpMessage exceptionMessage, Throwable cause) {

        super(exceptionMessage.toPrettyString(), cause);
    }


}
