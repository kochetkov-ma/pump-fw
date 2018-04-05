package ru.mk.pump.commons.utils;

import java.util.concurrent.Callable;
import lombok.Getter;
import lombok.NonNull;

public class CallableExt<T> implements Callable<T> {

    private final Callable<T> tCallable;

    @Getter
    private final String description;

    private CallableExt(@NonNull Callable<T> tCallable, @NonNull String description) {
        this.tCallable = tCallable;
        this.description = description;
    }

    public static <V> CallableExt<V> of(@NonNull Callable<V> tCallable) {
        return new CallableExt<>(tCallable, Strings.empty());
    }

    public static <V> CallableExt<V> of(@NonNull String description) {
        return new CallableExt<>(() -> null, description);
    }

    public CallableExt<T> withCallable(@NonNull Callable<T> tCallable) {
        return new CallableExt<>(tCallable, description);
    }

    public CallableExt<T> withDescription(@NonNull String description) {
        return new CallableExt<>(tCallable, description);
    }

    @Override
    public T call() throws Exception {
        return tCallable.call();
    }
}