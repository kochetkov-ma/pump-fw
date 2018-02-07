package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SubElementsNotFoundException extends ElementException {

    private static final String TITLE = "Cannot find expected sub rules by conditions : '%s'";

    public SubElementsNotFoundException(String conditions) {
        super(new PumpMessage(String.format(TITLE, conditions)));
    }

    public SubElementsNotFoundException(String conditions, Throwable cause) {
        super(new PumpMessage(String.format(TITLE, conditions)), cause);
    }

    public SubElementsNotFoundException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public SubElementsNotFoundException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

}
