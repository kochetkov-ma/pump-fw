package ru.mk.pump.cucumber.plugin;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import cucumber.api.Result;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@ToString
@EqualsAndHashCode(of = {"url", "uuid", "source"})
public class Feature implements StrictInfo, PrettyPrinter {

    @Getter
    private final String url;
    @Getter
    private final UUID uuid;
    @Getter
    private final String source;
    @Getter
    private boolean finished;
    @Getter
    private Result.Type status;
    @Getter
    private Scenario nonPassedScenario;

    private Scenario activeScenario;
    @Getter
    private List<Scenario> executedScenarios = Lists.newArrayList();

    Feature(String url, String source) {
        this.source = source;
        this.uuid = UUID.randomUUID();
        this.url = url;
        this.status = Result.Type.UNDEFINED;
    }

    public boolean isOk() {
        return nonPassedScenario == null;
    }

    public Optional<Scenario> getLastExecutedScenario() {
        if (executedScenarios.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(Iterables.getLast(executedScenarios));
    }

    public Optional<Scenario> getActiveScenario() {
        return Optional.ofNullable(activeScenario);
    }

    public List<Scenario> nonPassedScenarios() {
        return executedScenarios.stream().filter(s -> s.getStatus() != Result.Type.PASSED).collect(Collectors.toList());
    }

    @Override
    public String toPrettyString() {
        return PumpMessage.of(this).toPrettyString();
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoBuilder("feature")
                .put("uuid", uuid.toString())
                .put("url", url)
                .put("source", source)
                .put("status", status.name())
                .put("scenarios", Integer.toString(executedScenarios.size()))
                .build();
    }

    void finish() {
        finished = true;
    }

    Feature startScenario(Scenario activeScenario) {
        this.activeScenario = activeScenario;
        return this;
    }

    Feature releaseScenario(Result result) {
        this.executedScenarios.add(updateStatus(activeScenario, result));
        this.activeScenario = null;
        return this;
    }

    private Scenario updateStatus(Scenario scenario, Result result) {
        if (result.getStatus() != Result.Type.PASSED) {
            nonPassedScenario = scenario;
        }
        scenario.setResult(result);
        this.status = result.getStatus();
        return scenario;
    }
}
