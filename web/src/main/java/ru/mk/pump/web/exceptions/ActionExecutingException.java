package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.elements.internal.interfaces.Action;

@SuppressWarnings("unused")
public class ActionExecutingException extends ElementException {

    private static final String TITLE = "Executing action '%s' error";

    public ActionExecutingException(Action action) {
        super(action.getTarget(), (m) -> message(m, null, action));
    }

    public ActionExecutingException(Action action, Throwable throwable) {
        super(action.getTarget(), (m) -> message(m, null, action), throwable);
    }

    public ActionExecutingException(String message, Action action) {
        super(action.getTarget(), (m) -> message(m, message, action));
    }

    public ActionExecutingException(String message, Action action, Throwable throwable) {
        super(action.getTarget(), (m) -> message(m, message, action), throwable);
    }

    private static PumpMessage message(PumpMessage message, String desc, Action action) {
        return message.withDesc(Strings.concat(StringConstants.DOT, String.format(TITLE, action.name()), desc))
            .addExtraInfo("action name", action.name())
            .addExtraInfo("action stage", action.getStage().name())
            .addExtraInfo("action parameters", action.getParameters().toString());
    }
}
