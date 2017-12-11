package ru.mk.pump.web.elements.api.listeners;

import ru.mk.pump.commons.listener.Event;
import ru.mk.pump.commons.listener.Listener;
import ru.mk.pump.web.elements.api.listeners.StateListener.StateEvent;
import ru.mk.pump.web.elements.internal.State;

public interface StateListener extends Listener<State, StateEvent> {

    @Override
    default void on(Event<State, StateEvent> event, Object... args) {
        switch (event.name()) {
            case BEFORE:
                onBefore(event.get());
                break;
            case FINISH:
                onFinish(event.get());
                break;
        }
    }

    void onBefore(State state);

    void onFinish(State state);

    enum StateEvent {
        BEFORE, FINISH
    }
}
