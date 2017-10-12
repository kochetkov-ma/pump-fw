package ru.mk.pump.commons.interfaces;

import static ru.mk.pump.commons.constants.StringConstants.AP;

import ru.mk.pump.commons.utils.Strings;

public interface Collator<T> {

    boolean collate(T expected, T actual);

    String getMessage();

    default T handle(T value){
        return value;
    }

    default String getClass(T object) {
        return AP + object.getClass().getSimpleName() + AP;
    }

    default String info(T object) {
        return Strings.oneLineConcat(AP + handle(object).toString() + AP, "типа", getClass(object));
    }

    default String actual(T object) {
        return Strings.oneLineConcat("Актуальное значение", info(object));
    }
}
