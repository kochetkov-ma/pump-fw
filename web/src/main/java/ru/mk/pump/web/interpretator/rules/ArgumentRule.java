package ru.mk.pump.web.interpretator.rules;

@SuppressWarnings("unused")
abstract class ArgumentRule<T> extends AbstractRule<T> {

    @Override
    public String[] getEscapes() {
        return new String[]{","};
    }
}