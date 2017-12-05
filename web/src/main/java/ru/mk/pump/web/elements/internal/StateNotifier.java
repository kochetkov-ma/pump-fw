package ru.mk.pump.web.elements.internal;

import java.util.Set;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.listener.AbstractNotifier;
import ru.mk.pump.web.elements.api.listeners.StateListener;
import ru.mk.pump.web.elements.api.listeners.StateListener.StateEvent;
import ru.mk.pump.web.elements.internal.interfaces.InternalState;

@SuppressWarnings("WeakerAccess")
@NoArgsConstructor
abstract class StateNotifier extends AbstractNotifier<InternalState, StateEvent, StateListener> {

    public StateNotifier(
        Set<StateListener> actionListeners) {
        super(actionListeners);
    }

    protected void notifyOnBefore(InternalState state) {
        notify(event(state, StateEvent.BEFORE));
    }

    protected void notifyOnFinish(InternalState state) {
        notify(event(state, StateEvent.FINISH));
    }
}
