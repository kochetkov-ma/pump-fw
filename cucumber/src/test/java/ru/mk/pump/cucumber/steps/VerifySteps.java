package ru.mk.pump.cucumber.steps;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Verifier;
import ru.mk.pump.web.utils.WebReporter;

@Slf4j
public class VerifySteps {

    private final Verifier verifier;

    public VerifySteps() {
        this.verifier = WebReporter.getVerifier();
    }

    @Given("^Verify - equals expected '(.+)' and actual '(.+)'$")
    public void equals(Object pumpkinExpected, Object pumpkinActual) {
        log.info("Step equals({},{})", pumpkinExpected, pumpkinActual);
        verifier.equals("Cucumber verification step", pumpkinExpected, pumpkinActual);
    }
}