package ru.mk.pump.web.exceptions;

import static ru.mk.pump.web.exceptions.ElementDiscoveryException.DISPATCHER;

import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.PageManager;

@SuppressWarnings({"unused", "WeakerAccess"})
public class PageManagerException extends AbstractWebException {

    static final String MANAGER = "page manager";

    public PageManagerException(String message) {
        super(message);
    }

    public PageManagerException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public PageManagerException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public PageManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageManagerException withPageManager(PageManager elementFactory) {
        addTarget(MANAGER, elementFactory);
        return this;
    }
}