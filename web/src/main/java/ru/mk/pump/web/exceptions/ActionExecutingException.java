package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.mk.pump.web.elements.internal.interfaces.Action;

import javax.annotation.Nullable;

import static java.lang.String.format;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor
public class ActionExecutingException extends WebException {

    public ActionExecutingException(@NonNull String title, @Nullable Action action) {
        this(title, action, null);
    }

    public ActionExecutingException(@NonNull String title, @Nullable Action action, @Nullable Throwable cause) {
        super(title, cause);
        withAction(action);
    }

    @Override
    protected ActionExecutingException withAction(@Nullable Action action) {
        return (ActionExecutingException) super.withAction(action);
    }
}