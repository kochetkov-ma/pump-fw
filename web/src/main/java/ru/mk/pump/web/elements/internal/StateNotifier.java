package ru.mk.pump.web.elements.internal;

import java.util.Set;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.listener.AbstractNotifier;
import ru.mk.pump.web.elements.api.listeners.StateListener;
import ru.mk.pump.web.elements.api.listeners.StateListener.StateEvent;

@SuppressWarnings({"WeakerAccess", "unused"})
@NoArgsConstructor
abstract class StateNotifier extends AbstractNotifier<State, StateEvent, StateListener> {

    public StateNotifier(
        Set<StateListener> actionListeners) {
        super(actionListeners);
    }

    protected void notifyOnBefore(State state) {
        notify(event(state, StateEvent.BEFORE));
    }

    protected void notifyOnFinish(State state) {
        notify(event(state, StateEvent.FINISH));
    }
}
