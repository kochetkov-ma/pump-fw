package ru.mk.pump.cucumber.plugin;

import cucumber.api.Result;
import cucumber.api.TestCase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
@ToString
@EqualsAndHashCode(of = {"uuid"})
public class Scenario implements PrettyPrinter, StrictInfo {

    @Getter
    private final UUID uuid;
    @Getter
    private final TestCase testCase;
    private Result result;

    Scenario(TestCase testCase) {
        this.uuid = UUID.randomUUID();
        this.testCase = testCase;
        this.result = null;
    }

    public Optional<Result> getResult() {
        return Optional.ofNullable(result);
    }

    void setResult(Result result) {
        this.result = result;
    }

    public Result.Type getStatus() {
        if (result == null) {
            return Result.Type.UNDEFINED;
        } else {
            return result.getStatus();
        }
    }

    @Override
    public String toPrettyString() {
        return PumpMessage.of(this).toPrettyString();
    }

    @Override
    public Map<String, String> getInfo() {
        StrictInfo.StringMapBuilder res = StrictInfo.infoBuilder("scenario")
                .put("uuid", uuid.toString())
                .put("name", testCase.getName())
                .put("designation", testCase.getScenarioDesignation());
        if (result != null) {
            res.put("result", result.getStatus().name());
        }
        return res.build();
    }
}
