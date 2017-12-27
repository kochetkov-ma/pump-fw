package ru.mk.pump.web.exceptions;

import ru.mk.pump.web.page.api.Page;

@SuppressWarnings("unused")
public class PageException extends AbstractWebException {

    public PageException(String title) {
        super(title);
    }

    public PageException(String title, Page page) {
        super(title);
    }

    public PageException(String title, Page page, Throwable cause) {
        super(title, cause);
    }

    public PageException(String title, Throwable cause) {
        super(title, cause);
    }

    public PageException withTargetPage(Page page) {
        addTarget(PAGE, page);
        withBrowser(page.getBrowser());
        return this;
    }
}
