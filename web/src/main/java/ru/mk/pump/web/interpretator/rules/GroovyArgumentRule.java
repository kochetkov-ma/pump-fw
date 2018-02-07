package ru.mk.pump.web.interpretator.rules;

import static ru.mk.pump.web.interpretator.PumpkinConstants.GROOVY_PATTERN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.ToString;
import ru.mk.pump.web.interpretator.PumpkinConstants;
import ru.mk.pump.commons.utils.Groovy;

@SuppressWarnings("WeakerAccess")
@ToString(exclude = "groovy")
final class GroovyArgumentRule extends ArgumentRule<Object> {

    private final Groovy groovy;

    public GroovyArgumentRule(Groovy groovy) {
        super();
        this.groovy = groovy;
    }

    @Override
    public boolean parseStart(String left, String right) {
        return right.matches(GROOVY_PATTERN);
    }

    @Override
    public boolean parseEnd(String left, String right) {
        return super.parseEnd(left, right) || left.endsWith("}");
    }

    @Override
    public Object toValue(String string) {
        Matcher matcher = Pattern.compile(PumpkinConstants.GROOVY_PATTERN).matcher(string);
        if (matcher.find()) {
            return groovy.evalGroovy(matcher.group(1));
        }
        return null;
    }
}
