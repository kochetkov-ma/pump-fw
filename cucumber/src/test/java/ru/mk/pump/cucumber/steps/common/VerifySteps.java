package ru.mk.pump.cucumber.steps.common;

import cucumber.api.java.en.Given;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Collators;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.cucumber.steps.AbstractSteps;
import ru.mk.pump.cucumber.transform.PParam;
import ru.mk.pump.web.utils.WebReporter;

@Slf4j
public class VerifySteps extends AbstractSteps {

    private static final String MSG = "Cucumber verification step";

    private final Verifier verifier;

    public VerifySteps() {
        this.verifier = WebReporter.getVerifier();
    }

    @Given("^Verify - equals object expected '(.+)' and actual '(.+)'$")
    public void equalsObject(@PParam Object expect, @PParam Object actual) {
        verifier.equals(MSG, expect, actual);
    }

    @Given("^Verify - equals string expected '(.+)' and actual '(.+)'$")
    public void equalsString(@PParam String expect, @PParam String actual) {
        verifier.equals(MSG, Strings.liteNormalize(expect), Strings.liteNormalize(actual));
    }

    @Given("^Verify - equals long expected '(.+)' and actual '(.+)'$")
    public void equalsNumber(@PParam long expect, @PParam long actual) {
        verifier.equals(MSG, expect, actual);
    }

    @Given("^Verify - equals list expected '(.+)' and actual '(.+)'$")
    public void equalsList(List<String> expect, List<String> actual) {
        verifier.listEquals(MSG, expect, actual, Collators.equals(), null);
    }

    @Given("^Verify - contains string expected '(.+)' to actual '(.+)'$")
    public void contains(@PParam String expect, @PParam String actual) {
        verifier.contains(MSG, Strings.liteNormalize(expect), Strings.liteNormalize(actual));
    }

    @Given("^Verify - contains list expected '(.+)' and actual '(.+)'$")
    public void containsList(List<String> expect, List<String> actual) {
        verifier.listStrictContains(MSG, expect, actual, Collators.liteNormalizeContains(), null);
    }

    @Given("^Verify - actual '(.+)' is true$")
    public void isTrue(@PParam boolean actual) {
        verifier.checkTrue(MSG, actual);
    }

    @Given("^Verify - actual '(.+)' is false$")
    public void isFalse(@PParam boolean actual) {
        verifier.checkFalse(MSG, actual);
    }
}