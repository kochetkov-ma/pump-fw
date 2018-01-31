package ru.mk.pump.web.elements.internal;

import java.util.Set;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.listener.AbstractNotifier;
import ru.mk.pump.web.elements.api.listeners.ActionListener;
import ru.mk.pump.web.elements.api.listeners.ActionListener.ActionEvent;
import ru.mk.pump.web.elements.internal.interfaces.Action;

@SuppressWarnings("WeakerAccess")
@NoArgsConstructor
abstract class ActionNotifier extends AbstractNotifier<Action, ActionEvent, ActionListener> {

    public ActionNotifier(
        Set<ActionListener> actionListeners) {
        super(actionListeners);
    }

    protected void notifyOnAfterActionFail(Action action) {
        notify(event(action, ActionEvent.AFTER_ACTION_FAIL));
    }

    protected void notifyOnFail(Action action, Throwable throwable) {
        notify(event(action, ActionEvent.FAIL), throwable);
    }

    protected void notifyOnFinallyStateCheck(Action action) {
        notify(event(action, ActionEvent.FINALLY_STATE_CHECK));
    }

    protected void notifyOnSuccess(Action action, Object result) {
        notify(event(action, ActionEvent.SUCCESS), result);
    }

    protected void notifyOnBeforeSuccess(Action action) {
        notify(event(action, ActionEvent.BEFORE_ACTION_SUCCESS));
    }
}
