package ru.mk.pump.web.exceptions;

import static ru.mk.pump.web.exceptions.ElementDiscoveryException.DISPATCHER;

import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ElementFactoryException extends AbstractWebException {

    static final String PARENT = "parent_element";

    static final String FACTORY = "element_factory";

    public ElementFactoryException(String message) {
        super(message);
    }

    public ElementFactoryException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ElementFactoryException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public ElementFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElementFactoryException withTargetFactory(ElementFactory elementFactory) {
        addTarget(FACTORY, elementFactory);
        addTarget(DISPATCHER, elementFactory.getElementImplDispatcher());
        withBrowser(elementFactory.getBrowser());
        withPage(elementFactory.getPage());

        return this;
    }

    public ElementFactoryException withTargetParent(InternalElement element) {
        addTarget(PARENT, element);
        return this;
    }

    ElementFactoryException withFactory(ElementFactory elementFactory) {
        addEnv(FACTORY, elementFactory);
        addEnv(DISPATCHER, elementFactory.getElementImplDispatcher());
        withBrowser(elementFactory.getBrowser());
        withPage(elementFactory.getPage());
        return this;
    }
}