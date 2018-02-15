package ru.mk.pump.web.interpretator.items;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@SuppressWarnings("unused")
@EqualsAndHashCode
abstract class AbstractItem<T> implements Item<T> {

    @Getter
    private final T source;

    AbstractItem(T source) {
        this.source = source;
    }
}