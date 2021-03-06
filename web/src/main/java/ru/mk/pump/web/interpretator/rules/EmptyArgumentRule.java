package ru.mk.pump.web.interpretator.rules;

import javax.annotation.Nullable;
import lombok.ToString;

@SuppressWarnings("unused")
@ToString
final class EmptyArgumentRule extends ArgumentRule<String> {

    @Override
    public boolean parseStart(String left, String right) {
        return right.startsWith("()");
    }

    @Override
    public boolean parseEndOr(String left, String right) {
        return left.endsWith(")") || left.endsWith(",");
    }

    @Override
    public int minSize() {
        return 0;
    }

    @Nullable
    @Override
    public String value(String string) {
        return null;
    }
}
