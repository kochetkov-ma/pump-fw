package ru.mk.pump.web.elements.api.listeners;

import ru.mk.pump.commons.listener.Event;
import ru.mk.pump.commons.listener.Listener;
import ru.mk.pump.web.elements.api.listeners.ActionListener.ActionEvent;
import ru.mk.pump.web.elements.internal.interfaces.Action;

public interface ActionListener extends Listener<Action, ActionEvent> {

    @Override
    default void on(Event<Action, ActionEvent> event, Object... args) {
        switch (event.name()) {
            case AFTER:
                onAfter(event.get());
                break;
            case FAIL:
                onFail(event.get(), Listener.getFromArgsOrNull(Throwable.class, 0, args));
                break;
            case FINALLY_AFTER:
                onFinallyAfter(event.get());
                break;
            case SUCCESS:
                onSuccess(event.get(), Listener.getFromArgsOrNull(Object.class, 0, args));
                break;
        }
    }

    void onAfter(Action action);

    void onFinallyAfter(Action action);

    void onFail(Action action, Throwable throwable);

    void onSuccess(Action action, Object result);

    enum ActionEvent {
        AFTER, FAIL, FINALLY_AFTER, SUCCESS
    }
}
