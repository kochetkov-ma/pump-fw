package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ElementException extends AbstractWebException {

    static final String ELEMENT = "element";

    private static final String TITLE = "Pump element '%s' error";

    public ElementException(String title) {
        super(title);
    }

    public ElementException(String title, Throwable cause) {
        super(title, cause);
    }

    public ElementException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ElementException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    private static PumpMessage message(Action action) {
        return new PumpMessage(String.format(TITLE, action.name()))
            .addExtraInfo(action);
    }

    public ElementException withTargetElement(InternalElement element) {
        addTarget(ELEMENT, element);
        withBrowser(element.getBrowser());
        withPage(element.getPage());
        return this;
    }

    ElementException withElement(InternalElement element) {
        addEnv(ELEMENT, element);
        withBrowser(element.getBrowser());
        withPage(element.getPage());
        return this;
    }
}
