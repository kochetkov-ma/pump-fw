package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.page.api.Page;


public abstract class AbstractWebException extends PumpException {

    static final String BROWSER = "browser";

    static final String PAGE = "page";

    AbstractWebException(String title) {
        super(title);
    }

    AbstractWebException(String title, Throwable cause) {
        super(title, cause);
    }

    AbstractWebException(PumpMessage exceptionMessage) {
        super(exceptionMessage);
    }

    AbstractWebException(PumpMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }


    AbstractWebException withBrowser(Browser browser) {
        addEnv(BROWSER, browser);
        return this;
    }

    AbstractWebException withPage(Page page) {
        addEnv(PAGE, page);
        return this;
    }

    public AbstractWebException withTargetInfo(String name, StrictInfo targetInfo) {
        addTarget(name, targetInfo);
        return this;
    }
}
