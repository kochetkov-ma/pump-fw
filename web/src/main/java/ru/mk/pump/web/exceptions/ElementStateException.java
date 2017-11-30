package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalState;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ElementStateException extends ElementException {

    static final String STATE = "state";

    public ElementStateException(String title) {
        super(title);
    }

    public ElementStateException(String title, Throwable cause) {
        super(title, cause);
    }

    public ElementStateException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ElementStateException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public ElementStateException withTargetState(InternalState<?> state) {
        addTarget(STATE, state);
        return this;
    }

    public ElementException withElement(InternalElement element) {
        return super.withElement(element);
    }
}
