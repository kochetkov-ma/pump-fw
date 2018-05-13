package test.guice;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import cucumber.api.java.en.Given;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.utils.TestVars;

@Slf4j
public class GuiceSteps {

    @Getter
    private final double pi;
    @Getter
    private final TestVars testVars;

    @Inject
    public GuiceSteps(@Named("Pi number") double pi, TestVars testVars) {
        this.pi = pi;
        this.testVars = testVars;
    }

    @Given("^guice step main$")
    public void guiceTest() {
        log.info("[TEST] {}", pi);
        log.info("[TEST] {}", Strings.toString(testVars));
    }
}