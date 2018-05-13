package test.guice;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import test.CucumberRunner;

@RunWith(CucumberRunner.class)
@CucumberOptions(
        strict = true,
        plugin = "ru.mk.pump.cucumber.plugin.PumpCucumberPlugin",
        glue = {"test.guice", "ru.mk.pump.cucumber"},
        features = "classpath:guice/guice.feature")
public class GuiceTest {

}
