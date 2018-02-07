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
    public boolean parseEnd(String left, String right) {
        return super.parseEnd(left, right) || !right.matches(PumpkinConstants.TITLE_PATTERN);
    }

    @Override
    public String[] getEscapes() {
        return new String[0];
    }

    @Override
    public String toValue(String string) {
        return string;
    }
}