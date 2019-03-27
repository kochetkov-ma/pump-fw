package ru.mk.pump.web.browsers.api;

import ru.mk.pump.commons.listener.Event;
import ru.mk.pump.commons.listener.Listener;

public interface BrowserListener extends Listener<Browser, BrowserListener.BrowserEvent> {
    @Override
    default void on(Event<Browser, BrowserListener.BrowserEvent> event, Object... args) {
        switch (event.name()) {
            case OPEN:
                onOpen(event.get());
                break;
            case CLOSE:
                onClose(event.get(), Listener.getFromArgsOrNull(Throwable.class, 0, args));
                break;
            case OPEN_TAB:
                onOpenTab(event.get(), Listener.getFromArgsOrNull(String.class, 0, args));
                break;
            case CLOSE_TAB:
                onCloseTab(event.get(), Listener.getFromArgsOrNull(String.class, 0, args));
                break;
        }
    }

    void onOpen(Browser browser);

    void onClose(Browser browser, Throwable fromArgsOrNull);

    void onOpenTab(Browser browser, String tabUuid);

    void onCloseTab(Browser browser, String tabUuid);

    enum BrowserEvent {
        OPEN, CLOSE, OPEN_TAB, CLOSE_TAB
    }
}