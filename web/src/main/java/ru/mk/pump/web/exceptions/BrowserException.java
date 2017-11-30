package ru.mk.pump.web.exceptions;

import ru.mk.pump.web.browsers.Browser;

@SuppressWarnings("unused")
public class BrowserException extends AbstractWebException {

    public BrowserException(String title) {
        super(title);
    }

    public BrowserException(String title, Browser browser) {
        super(title);
        withTargetBrowser(browser);
    }

    public BrowserException(String title, Browser browser, Throwable cause) {
        super(title, cause);
        withTargetBrowser(browser);
    }

    public BrowserException(String title, Throwable cause) {
        super(title, cause);
    }

    public BrowserException withTargetBrowser(Browser browser) {
        addTarget(BROWSER, browser);
        return this;
    }
}
