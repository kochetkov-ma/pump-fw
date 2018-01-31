package ru.mk.pump.web.elements.api.listeners;

import org.apache.commons.lang3.tuple.Pair;
import ru.mk.pump.commons.listener.Event;
import ru.mk.pump.commons.listener.Listener;
import ru.mk.pump.web.elements.api.listeners.StateListener.StateEvent;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

public interface StateListener extends Listener<Pair<State, InternalElement>, StateEvent> {

    @Override
    default void on(Event<Pair<State, InternalElement>, StateEvent> event, Object... args) {
        switch (event.name()) {
            case BEFORE:
                onBefore(event.get());
                break;
            case FINISH:
                onFinish(event.get());
                break;
        }
    }

    void onBefore(Pair<State, InternalElement> state);

    void onFinish(Pair<State, InternalElement> state);

    enum StateEvent {
        BEFORE, FINISH
    }
}
