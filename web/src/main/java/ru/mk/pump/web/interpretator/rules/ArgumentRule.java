package ru.mk.pump.web.interpretator.rules;

@SuppressWarnings("unused")
abstract class ArgumentRule<T> extends AbstractRule<T> {

    @Override
    public boolean parseEnd(String left, String right) {
        return super.parseEnd(left, right);
    }

    @Override
    public String[] getEscapes() {
        return new String[]{","};
    }
}