package ru.mk.pump.web.page.api;

import ru.mk.pump.commons.listener.Event;
import ru.mk.pump.commons.listener.Listener;
import ru.mk.pump.web.page.api.PageListener.PageEvent;

public interface PageListener extends Listener<Page, PageEvent> {

    @Override
    default void on(Event<Page, PageEvent> event, Object... args) {
        switch (event.name()) {
            case BEFORE_LOAD:
                onBeforeLoad(event.get());
                break;
            case LOAD_FAIL:
                onLoadFail(event.get(), Listener.getFromArgsOrNull(Throwable.class, 0, args));
                break;
            case LOAD_SUCCESS:
                onLoadSuccess(event.get());
                break;
        }
    }

    void onLoadSuccess(Page page);

    void onLoadFail(Page page, Throwable fromArgsOrNull);

    void onBeforeLoad(Page page);

    public enum PageEvent {
        BEFORE_LOAD, LOAD_FAIL, LOAD_SUCCESS;
    }
}
