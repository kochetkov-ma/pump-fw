package ru.mk.pump.web.interpretator.rules;

import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.utils.Strings;

@SuppressWarnings("unused")
public interface Rule<T> {

    boolean parseStart(String left, String right);

    boolean parseEnd(String left, String right);

    String[] getEscapes();

    @Nullable
    T toValue(String string);
}
