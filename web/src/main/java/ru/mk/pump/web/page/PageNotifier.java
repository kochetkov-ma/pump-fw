package ru.mk.pump.web.page;

import lombok.NoArgsConstructor;
import ru.mk.pump.commons.listener.AbstractNotifier;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.web.page.api.PageListener;
import ru.mk.pump.web.page.api.PageListener.PageEvent;

@SuppressWarnings({"WeakerAccess", "unused"})
@NoArgsConstructor
abstract class PageNotifier extends AbstractNotifier<Page, PageEvent, PageListener> {

    protected void notifyOnBeforeLoad(Page page) {
        notify(event(page, PageEvent.BEFORE_LOAD));
    }

    protected void notifyOnLoadFail(Page page, Throwable throwable) {
        notify(event(page, PageEvent.LOAD_FAIL));
    }

    protected void notifyOnLoadSuccess(Page page) {
        notify(event(page, PageEvent.LOAD_SUCCESS));
    }
}
