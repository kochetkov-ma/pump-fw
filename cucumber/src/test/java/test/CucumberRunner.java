package test;

import cucumber.api.junit.Cucumber;
import org.junit.runners.model.InitializationError;

import java.io.IOException;

public class CucumberRunner extends Cucumber {
    static {
        System.setProperty("pump.cucumber.configuration.path", "guice/guice.cucumber.properties");
    }

    /**
     * Constructor called by JUnit.
     *
     * @param clazz the class with the @RunWith annotation.
     *
     * @throws InitializationError if there is another problem
     */
    public CucumberRunner(Class clazz) throws InitializationError {
        super(clazz);
    }
}
