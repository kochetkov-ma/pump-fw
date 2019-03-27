package ru.mk.pump.commons.interfaces;

import ru.mk.pump.commons.utils.Str;

import static ru.mk.pump.commons.constants.StringConstants.AP;

public interface Collator<T> {

    boolean collate(T expected, T actual);

    String getMessage();

    default T handle(T value) {
        return value;
    }

    default String getClass(T object) {
        return AP + object.getClass().getSimpleName() + AP;
    }

    default String info(T object) {
        return Str.space(AP + handle(object).toString() + AP, "типа", getClass(object));
    }

    default String actual(T object) {
        return Str.space("Актуальное значение", info(object));
    }
}
