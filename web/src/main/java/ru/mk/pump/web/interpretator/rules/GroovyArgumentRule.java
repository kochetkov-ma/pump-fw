package ru.mk.pump.web.interpretator.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.ToString;
import ru.mk.pump.commons.utils.Groovy;
import ru.mk.pump.web.interpretator.PumpkinConstants;

@SuppressWarnings("WeakerAccess")
@ToString(exclude = "groovy")
final class GroovyArgumentRule extends ArgumentRule<Object> implements SeparateValue {

    private final static String GROOVY_PATTERN = "^\\$?groovy\\{(.*)}$";

    private final Groovy groovy;

    public GroovyArgumentRule(Groovy groovy) {
        super();
        this.groovy = groovy;
    }

    @Override
    public boolean parseStart(String left, String right) {
        return right.matches(PumpkinConstants.GROOVY_PATTERN);
    }

    @Override
    public boolean parseEndOr(String left, String right) {
        return left.endsWith("}");
    }

    @Override
    public Object value(String string) {
        Matcher matcher = Pattern.compile(GROOVY_PATTERN).matcher(string);
        if (matcher.find()) {
            return groovy.evalGroovy(matcher.group(1));
        }
        return null;
    }
}
