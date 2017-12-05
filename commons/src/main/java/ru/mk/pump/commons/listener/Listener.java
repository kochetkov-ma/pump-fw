package ru.mk.pump.commons.listener;

import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public interface Listener<T, V extends Enum<V>> {

    @SuppressWarnings("unchecked")
    @Nullable
    static <R> R getFromArgsOrNull(@NotNull Class<R> clazz, int index, @Nullable Object... args) {
        if (args != null && args.length > index + 1 && clazz.isAssignableFrom(args[index].getClass())) {
            return (R) args[index];
        } else {
            return null;
        }
    }

    void on(Event<T, V> event, Object... args);
}