package ru.mk.pump.web.elements.api.listeners;

import ru.mk.pump.commons.listener.Event;
import ru.mk.pump.commons.listener.Listener;
import ru.mk.pump.web.elements.api.listeners.StateListener.StateEvent;
import ru.mk.pump.web.elements.internal.interfaces.InternalState;

public interface StateListener extends Listener<InternalState, StateEvent> {

    @Override
    default void on(Event<InternalState, StateEvent> event, Object... args) {
        switch (event.name()) {
            case BEFORE:
                onBefore(event.get());
                break;
            case FINISH:
                onFinish(event.get());
                break;
        }
    }

    void onBefore(InternalState state);

    void onFinish(InternalState state);

    enum StateEvent {
        BEFORE, FINISH
    }
}
