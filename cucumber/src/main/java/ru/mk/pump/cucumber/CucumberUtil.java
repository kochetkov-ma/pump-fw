package ru.mk.pump.cucumber;

import com.google.common.collect.ImmutableMap;
import cucumber.api.Scenario;
import lombok.experimental.UtilityClass;
import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import ru.mk.pump.commons.utils.Str;

@SuppressWarnings("unused")
@UtilityClass
public class CucumberUtil {

    /**
     * @throws AssumptionViolatedException - throw AssumptionViolatedException to skip test
     * @param scenario Scenario
     */
    public void skipScenario(Scenario scenario) throws AssumptionViolatedException {
        //noinspection ConstantConditions
        Assume.assumeTrue("[SKIPPED] " + Str.space(scenario.getName(), scenario.getId()), false);
    }

    public String toPrettyString(Scenario scenario) {
        return Str.toPrettyString(ImmutableMap.builder()
                .put("type", scenario.getClass().getSimpleName())
                .put("name", scenario.getName())
                .put("status", scenario.getStatus().name())
                .put("id", scenario.getId())
                .put("uri", scenario.getUri())
                .put("tags", Str.toString(scenario.getSourceTagNames()))
                .put("size", scenario.getLines().size())
                .build()
        );
    }
}
