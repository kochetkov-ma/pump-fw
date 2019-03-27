package ru.mk.pump.commons.exception;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class UtilException extends PumpException {

    public UtilException(@Nullable String title) {
        super(title);
    }

    public UtilException(@Nullable String title, @Nullable Throwable cause) {
        super(title, cause);
    }
}