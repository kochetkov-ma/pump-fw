package ru.mk.pump.commons.listener;

import javax.annotation.Nullable;
import lombok.NonNull;

public interface Listener<T, V extends Enum<V>> {

    @SuppressWarnings("unchecked")
    @Nullable
    static <R> R getFromArgsOrNull(@NonNull Class<R> clazz, int index, @Nullable Object... args) {
        if (args != null && args.length > index && clazz.isAssignableFrom(args[index].getClass())) {
            return (R) args[index];
        } else {
            return null;
        }
    }

    void on(Event<T, V> event, Object... args);
}