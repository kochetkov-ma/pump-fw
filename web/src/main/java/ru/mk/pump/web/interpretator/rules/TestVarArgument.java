package ru.mk.pump.web.interpretator.rules;

import static ru.mk.pump.web.interpretator.PumpkinConstants.TEST_VAR_PATTERN;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.ToString;
import ru.mk.pump.web.interpretator.PumpkinConstants;

@SuppressWarnings({"unused", "WeakerAccess"})
@ToString
final class TestVarArgument extends ArgumentRule<Object> {

    private final Map<String, Object> testVars;

    public TestVarArgument(Map<String, Object> testVars) {
        this.testVars = testVars;
    }

    @Override
    public boolean parseStart(String left, String right) {
        return right.matches(TEST_VAR_PATTERN);
    }

    @Override
    public boolean parseEndOr(String left, String right) {
        return left.endsWith("}");
    }

    @Override
    public Object value(String string) {
        Matcher matcher = Pattern.compile(PumpkinConstants.TEST_VAR_PATTERN).matcher(string);
        if (matcher.find()) {
            return testVars.get(matcher.group(1));
        }
        return null;
    }
}