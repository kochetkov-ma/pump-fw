package ru.mk.pump.web.interpretator.items;

import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("unused")
abstract class AbstractItem<T> implements Item<T> {

    @Getter
    private final T source;

    AbstractItem(T source) {
        this.source = source;
    }
}