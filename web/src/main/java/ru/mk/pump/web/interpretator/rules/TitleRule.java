package ru.mk.pump.web.interpretator.rules;

import lombok.ToString;
import ru.mk.pump.web.interpretator.PumpkinConstants;

@ToString
final class TitleRule extends AbstractRule<String> {

    @Override
    public boolean parseStart(String left, String right) {
        return right.matches(PumpkinConstants.TITLE_PATTERN);
    }

    @Override
    public boolean parseEndOr(String left, String right) {
        return !right.matches(PumpkinConstants.TITLE_PATTERN);
    }

    @Override
    public String value(String string) {
        return string;
    }
}