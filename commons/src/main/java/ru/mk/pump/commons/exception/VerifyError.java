package ru.mk.pump.commons.exception;

public class VerifyError extends AssertionError {

    private static final String PRE = "Pump verify fail";

    public VerifyError(PumpMessage detailMessage) {
        super(detailMessage.withPre(PRE).toPrettyString());
    }

    public VerifyError(PumpMessage detailMessage, Throwable cause) {
        super(detailMessage.withPre(PRE).toPrettyString(), cause);
    }
}
