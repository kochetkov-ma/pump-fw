package ru.mk.pump.commons.listener;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings({"WeakerAccess", "unused"})
@NoArgsConstructor
public abstract class AbstractNotifier<T, V extends Enum<V>, L extends Listener<T, V>> {

    @Getter(AccessLevel.PROTECTED)
    private Set<L> actionListeners = new CopyOnWriteArraySet<>();

    protected AbstractNotifier(Set<L> actionListeners) {
        this.actionListeners.addAll(actionListeners);
    }

    public AbstractNotifier clearListeners() {
        actionListeners.clear();
        return this;
    }

    public AbstractNotifier addListener(L actionListener) {
        actionListeners.add(actionListener);
        return this;
    }

    protected void notify(Event<T, V> event) {
        actionListeners.forEach(i -> i.on(event));
    }

    protected void notify(Event<T, V> event, Object... args) {
        actionListeners.forEach(i -> i.on(event));
    }

    protected Event<T, V> event(T action, V type) {
        return new Event<T, V>() {

            @Override
            public T get() {
                return action;
            }

            @Override
            public V name() {
                return type;
            }
        };
    }
}