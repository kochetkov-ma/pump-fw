package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.internal.interfaces.Action;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ActionExecutingException extends AbstractWebException {

    static final String ACTION = "action";

    static final String TITLE = "Executing action '%s' error";

    public ActionExecutingException(Action action) {
        this(action, null);
    }

    public ActionExecutingException(Action action, Throwable throwable) {
        super(message(action), throwable);
        withBrowser(action.getTarget().getBrowser());
        withPage(action.getTarget().getPage());
        addTarget(ACTION, action);
    }

    public ActionExecutingException(String description, Action action) {
        this(action);
        getSourceMessage().withDesc(description);
    }

    public ActionExecutingException(String description, Action action, Throwable throwable) {
        this(action, throwable);
        getSourceMessage().withDesc(description);
    }

    private static PumpMessage message(Action action) {
        return new PumpMessage(String.format(TITLE, action.name()));
    }
}
