package ru.mk.pump.web.interpretator.rules;

import lombok.ToString;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    @Override
    public String value(String string) {
        return null;
    }
}
