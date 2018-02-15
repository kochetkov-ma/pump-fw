package ru.mk.pump.web.interpretator.rules;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public interface Rule<T> {

    boolean parseStart(String left, String right);

    boolean parseEnd(String left, String right);

    String[] getEscapes();

    @Nullable
    T toValue(String string);
}
