package ru.mk.pump.cucumber.glue.step.en;

import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Collators;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.cucumber.glue.AbstractSteps;

import java.util.List;

@Slf4j
public class VerifySteps extends AbstractSteps {

    private static final String MSG = "Cucumber verification step";

    private final Verifier verifier;

    @Inject
    public VerifySteps(Verifier verifier) {
        this.verifier = verifier;
    }

    @Given("^Verify - equals object expected '(.+)' and actual '(.+)'$")
    public void equalsObject(Object expect, Object actual) {
        verifier.equals(MSG, expect, actual);
    }

    @Given("^Verify - NOT equals object expected '(.+)' and actual '(.+)'$")
    public void notEqualsObject(Object expect, Object actual) {
        verifier.notEquals(MSG, expect, actual);
    }

    @Given("^Verify - equals string expected '(.+)' and actual '(.+)'$")
    public void equalsString(String expect, String actual) {
        verifier.equals(MSG, Str.liteNormalize(expect), Str.liteNormalize(actual));
    }

    @Given("^Verify - NOT equals string expected '(.+)' and actual '(.+)'$")
    public void notEqualsString(String expect, String actual) {
        verifier.notEquals(MSG, Str.liteNormalize(expect), Str.liteNormalize(actual));
    }

    @Given("^Verify - equals long expected '(.+)' and actual '(.+)'$")
    public void equalsNumber(long expect, long actual) {
        verifier.equals(MSG, expect, actual);
    }

    @Given("^Verify - NOT equals long expected '(.+)' and actual '(.+)'$")
    public void notEqualsNumber(long expect, long actual) {
        verifier.notEquals(MSG, expect, actual);
    }

    @Given("^Verify - equals list expected '(.+)' and actual '(.+)'$")
    public void equalsList(List<String> expect, List<String> actual) {
        verifier.listEquals(MSG, expect, actual, Collators.equals(), null);
    }

    @Given("^Verify - contains string expected '(.+)' to actual '(.+)'$")
    public void contains(String expect, String actual) {
        verifier.contains(MSG, Str.liteNormalize(expect), Str.liteNormalize(actual));
    }

    @Given("^Verify - NOT contains string expected '(.+)' to actual '(.+)'$")
    public void notContains(String expect, String actual) {
        verifier.notContains(MSG, Str.liteNormalize(expect), Str.liteNormalize(actual));
    }

    @Given("^Verify - contains list expected '(.+)' and actual '(.+)'$")
    public void containsList(List<String> expect, List<String> actual) {
        verifier.listStrictContains(MSG, expect, actual, Collators.liteNormalizeContains(), null);
    }

    @Given("^Verify - actual '(.+)' is true$")
    public void isTrue(boolean actual) {
        verifier.checkTrue(MSG, actual);
    }

    @Given("^Verify - actual '(.+)' is false$")
    public void isFalse(boolean actual) {
        verifier.checkFalse(MSG, actual);
    }
}