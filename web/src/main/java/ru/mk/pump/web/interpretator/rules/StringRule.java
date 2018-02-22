package ru.mk.pump.web.interpretator.rules;

import lombok.ToString;
import ru.mk.pump.web.interpretator.PumpkinConstants;

@SuppressWarnings("WeakerAccess")
@ToString
final class StringRule extends AbstractRule<String> implements SeparateValue {

    @Override
    public boolean parseStart(String left, String right) {
        return true;
    }

    @Override
    public boolean parseEndOr(String left, String right) {
        return right.matches(PumpkinConstants.GROOVY_PATTERN) || right.matches(PumpkinConstants.TEST_VAR_PATTERN);
    }

    @Override
    public String value(String string) {
        return string;
    }
}
