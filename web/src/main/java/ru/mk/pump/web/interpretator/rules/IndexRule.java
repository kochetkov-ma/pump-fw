package ru.mk.pump.web.interpretator.rules;

import lombok.ToString;

@SuppressWarnings("unused")
@ToString
final class IndexRule extends AbstractRule<Integer> {

    @Override
    public boolean parseStart(String left, String right) {
        return left.endsWith("[");
    }

    @Override
    public boolean parseEndOr(String left, String right) {
        return right.startsWith("]");
    }

    @Override
    public Integer value(String string) {
        return Integer.valueOf(string);
    }

}
