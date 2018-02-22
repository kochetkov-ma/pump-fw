package ru.mk.pump.web.interpretator.rules;

import javax.annotation.Nullable;
import lombok.ToString;

@SuppressWarnings("unused")
@ToString
final class StringArgumentRule extends ArgumentRule<String> {

    @Nullable
    @Override
    public String value(String string) {
        return string;
    }

    @Override
    public int minSize() {
        return 0;
    }

    @Override
    public boolean parseEndOr(String left, String right) {
        return right.startsWith(")") || right.startsWith(",");
    }

    @Override
    public boolean parseStart(String left, String right) {
        return left.endsWith("(") || left.endsWith(",");
    }

}
