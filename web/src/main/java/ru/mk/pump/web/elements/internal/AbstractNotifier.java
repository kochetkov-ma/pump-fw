package ru.mk.pump.web.elements.internal;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings({"WeakerAccess", "unused"})
@NoArgsConstructor
abstract class AbstractNotifier {

    @Getter(AccessLevel.PROTECTED)
    private Set<ActionListener> actionListeners = new CopyOnWriteArraySet<>();

    public AbstractNotifier(Set<ActionListener> actionListeners) {
        this.actionListeners = actionListeners;
    }

    public AbstractNotifier addListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
        return this;
    }

    protected void notifyOnAfter(Action action) {
        actionListeners.forEach(i -> i.onAfter(action));
    }

    protected void notifyOnFail(Action action, Throwable throwable) {
        actionListeners.forEach(i -> i.onFail(action, throwable));
    }

    protected void notifyOnFinallyAfter(Action action) {
        actionListeners.forEach(i -> i.onFinallyAfter(action));
    }

    protected void notifyOnSuccess(Action action, Object result) {
        actionListeners.forEach(i -> i.onSuccess(action, result));
    }
}
