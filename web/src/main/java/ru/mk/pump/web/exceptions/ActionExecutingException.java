package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.internal.Action;

@SuppressWarnings("unused")
public class ActionExecutingException extends PumpException {

    private static final String TITLE = "Executing action error";

    public ActionExecutingException(Action action) {
        super(message(null, action));
    }

    public ActionExecutingException(Action action, Throwable throwable) {
        super(message(null, action), throwable);
    }

    public ActionExecutingException(String message, Action action) {
        super(message(message, action));
    }

    public ActionExecutingException(String message, Action action, Throwable throwable) {
        super(message(message, action), throwable
        );
    }

    private static PumpMessage message(String desc, Action action) {
        return new PumpMessage(TITLE).withDesc(desc)
            .addExtraInfo("action name", action.name())
            .addExtraInfo("action stage", action.getStage().name())
            .addExtraInfo("action parameters", action.getParameters().toString());
    }
}
