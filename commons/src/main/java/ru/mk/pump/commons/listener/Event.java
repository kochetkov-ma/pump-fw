package ru.mk.pump.commons.listener;

public interface Event<T, V extends Enum<V>> {

    T get();

    V name();
}
