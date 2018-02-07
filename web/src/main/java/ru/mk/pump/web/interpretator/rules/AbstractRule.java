package ru.mk.pump.web.interpretator.rules;

import java.util.Arrays;
import ru.mk.pump.commons.utils.Strings;

abstract class AbstractRule<T> implements Rule<T> {

    @Override
    public boolean parseEnd(String left, String right) {
        return Strings.isEmpty(right) && Arrays.stream(getEscapes()).noneMatch(esc -> right.startsWith('/' + esc));
    }
}