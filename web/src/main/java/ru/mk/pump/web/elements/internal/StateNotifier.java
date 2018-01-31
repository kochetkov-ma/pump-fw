package ru.mk.pump.web.elements.internal;

import java.util.Set;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import ru.mk.pump.commons.listener.AbstractNotifier;
import ru.mk.pump.web.elements.api.listeners.StateListener;
import ru.mk.pump.web.elements.api.listeners.StateListener.StateEvent;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

@SuppressWarnings({"WeakerAccess", "unused"})
@NoArgsConstructor
abstract class StateNotifier extends AbstractNotifier<Pair<State, InternalElement>, StateEvent, StateListener> {

    public StateNotifier(
        Set<StateListener> actionListeners) {
        super(actionListeners);
    }

    protected void notifyOnBefore(Pair<State, InternalElement> state) {
        notify(event(state, StateEvent.BEFORE));
    }

    protected void notifyOnFinish(Pair<State, InternalElement> state) {
        notify(event(state, StateEvent.FINISH));
    }
}
