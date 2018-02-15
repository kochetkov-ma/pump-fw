package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.ElementImplDispatcher;

@SuppressWarnings("unused")
public class ElementDiscoveryException extends PumpException {

    static final String DISPATCHER = "element_dispatcher";

    public ElementDiscoveryException(String message) {
        super(message);
    }

    public ElementDiscoveryException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ElementDiscoveryException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public ElementDiscoveryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElementDiscoveryException withTargetDispatcher(ElementImplDispatcher targetDispatcher) {
        addTarget(DISPATCHER, targetDispatcher);
        return this;
    }

    ElementDiscoveryException withDispatcher(ElementImplDispatcher targetDispatcher) {
        addEnv(DISPATCHER, targetDispatcher);
        return this;
    }
}
