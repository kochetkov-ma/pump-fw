package ru.mk.pump.web.interpretator.rules;

import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
@ToString
final class StringArgumentRule extends ArgumentRule<String> {

    @Nullable
    @Override
    public String toValue(String string) {
        return string;
    }

    @Override
    public boolean parseEnd(String left, String right) {
        return super.parseEnd(left, right) || right.startsWith(")") || right.startsWith(",");
    }

    @Override
    public boolean parseStart(String left, String right) {
        return left.endsWith("(") || left.endsWith(",");
    }

}
