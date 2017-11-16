package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.ThrowableMessage;
import ru.mk.pump.web.elements.internal.InternalElement;

@SuppressWarnings("unused")
public class ElementException extends PumpException {

    private static final String TITLE = "Pump element error";

    public ElementException(String message) {
        super(message);
    }

    public ElementException(InternalElement element) {
        super(message(null, element));
    }

    public ElementException(InternalElement element, Throwable throwable) {
        super(message(null, element), throwable);
    }

    public ElementException(String message, InternalElement element) {
        super(message(message, element));
    }

    public ElementException(String message, InternalElement element, Throwable throwable) {
        super(message(message, element), throwable
        );
    }

    private static ThrowableMessage message(String desc, InternalElement element) {
        return new ThrowableMessage(TITLE).withDesc(desc)
            .addExtraInfo("type", element.getClass().getSimpleName())
            .addExtraInfo("is list", String.valueOf(element.isList()))
            .addExtraInfo("name", element.getName())
            .addExtraInfo("by", element.getBy().toString())
            .addEnvInfo("browser id", element.getBrowser().getId())
            .addEnvInfo("browser config", element.getBrowser().toString());
    }
}
