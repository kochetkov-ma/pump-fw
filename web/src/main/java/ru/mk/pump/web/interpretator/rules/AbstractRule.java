package ru.mk.pump.web.interpretator.rules;

import static ru.mk.pump.web.interpretator.PumpkinConstants.ESC_CHAR;

import java.util.Arrays;
import javax.annotation.Nullable;
import ru.mk.pump.commons.utils.Strings;

abstract class AbstractRule<T> implements Rule<T> {

    @Override
    public final boolean parseEnd(String left, String right) {
        return Strings.isEmpty(right) || parseEndOr(left, right) && Arrays.stream(getEscapes())
            .noneMatch(esc -> left.endsWith(ESC_CHAR) && right.startsWith(esc));
    }

    abstract protected boolean parseEndOr(String left, String right);

    abstract protected T value(String string);

    @Nullable
    @Override
    public final T toValue(String string) {
        return value(Arrays.stream(getEscapes()).reduce(string, (str, esc) -> str.replaceAll(ESC_CHAR + esc, esc)));
    }

    public String[] getEscapes() {
        return new String[0];
    }
}