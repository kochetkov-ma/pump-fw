package ru.mk.pump.web.browsers;

import ru.mk.pump.commons.listener.AbstractNotifier;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.browsers.api.BrowserListener;
import ru.mk.pump.web.browsers.api.BrowserListener.BrowserEvent;

class BrowserNotifier extends AbstractNotifier<Browser, BrowserEvent, BrowserListener> {

    protected void notifyOnOpen(Browser browser) {
        notify(event(browser, BrowserEvent.OPEN));
    }

    protected void notifyOnClose(Browser browser, Throwable throwable) {
        notify(event(browser, BrowserEvent.CLOSE));
    }

    protected void notifyOnOpenTab(Browser browser, String tabUuid) {
        notify(event(browser, BrowserEvent.OPEN_TAB));
    }

    protected void notifyOnCloseTab(Browser browser, String tabUuid) {
        notify(event(browser, BrowserEvent.CLOSE_TAB));
    }
}


