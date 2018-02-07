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
    public boolean parseEnd(String left, String right) {
        return super.parseEnd(left, right) || right.startsWith("]");
    }

    @Override
    public String[] getEscapes() {
        return new String[0];
    }

    @Override
    public Integer toValue(String string) {
        return Integer.valueOf(string);
    }

}
