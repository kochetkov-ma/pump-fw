package ru.mk.pump.web.elements.internal;

import com.google.common.collect.Sets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import lombok.NonNull;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.elements.enums.StateType;

@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class State implements StrictInfo, PrettyPrinter{

    private final StateType stateType;

    private WaitResult<Boolean> result;

    private String name;

    private Set<Callable<Boolean>> payload = Sets.newLinkedHashSet();

    private Consumer<WaitResult<Boolean>> tearDown;

    protected State(@NonNull StateType stateType, @NonNull Set<Callable<Boolean>> payload, @Nullable Consumer<WaitResult<Boolean>> tearDown) {
        this.payload.addAll(payload);
        this.stateType = stateType;
        this.name = stateType.toString();
        this.tearDown = tearDown;
    }

    public static State of(@NonNull StateType stateType, @NonNull Set<Callable<Boolean>> payload) {
        return new State(stateType, payload, null);
    }

    @SuppressWarnings("unchecked")
    public static State of(@NonNull StateType stateType, @NonNull Callable<Boolean> payload) {
        return new State(stateType, Sets.newHashSet(payload), null);
    }

    @SuppressWarnings("unchecked")
    public static State of(@NonNull StateType stateType, @NonNull Callable<Boolean> payload, Consumer<WaitResult<Boolean>> tearDown) {
        return new State(stateType, Sets.newHashSet(payload), tearDown);
    }

    @NonNull
    public Set<Callable<Boolean>> get() {
        return payload;
    }

    public StateType type() {
        return stateType;
    }

    public String name() {
        return name;
    }

    public State withName(String name) {
        this.name = name;
        return this;
    }

    public Optional<Consumer<WaitResult<Boolean>>> getTearDown() {
        return Optional.ofNullable(tearDown);
    }

    public State withTearDown(Consumer<WaitResult<Boolean>> tearDown) {
        this.tearDown = tearDown;
        return this;
    }

    @Override
    public String toPrettyString() {
        return Strings.toPrettyString(getInfo());
    }

    protected State withPayload(Set<Callable<Boolean>> payload) {
        this.payload.addAll(payload);
        return this;
    }

    public State setResult(@Nullable WaitResult<Boolean> result) {
        this.result = result;
        return this;
    }

    public WaitResult<Boolean> result() {
        return result;
    }

    public boolean isResolved() {
        return result != null;
    }

    @Override
    public Map<String, String> getInfo() {
        final LinkedHashMap<String, String> result = new LinkedHashMap<>();
        result.put("type", getClass().getSimpleName());
        result.put("stateType", stateType.toString());
        result.put("result", Strings.toString(this.result));
        result.put("name", name);
        return result;
    }

    @Override
    public int hashCode() {
        int result1 = stateType != null ? stateType.hashCode() : 0;
        result1 = 31 * result1 + (result != null ? result.getResult().hashCode() : 0);
        result1 = 31 * result1 + (name != null ? name.hashCode() : 0);
        result1 = 31 * result1 + (payload != null ? payload.getClass().hashCode() : 0);
        result1 = 31 * result1 + (tearDown != null ? 1 : 0);
        return result1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        State that = (State) o;

        if (stateType != that.stateType) {
            return false;
        }
        if (!Objects.equals(result != null ? result.getResult() : null, that.result != null ? that.result.getResult() : null)) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        //noinspection SimplifiableIfStatement
        if (!Objects.equals(payload != null ? payload.getClass() : null, that.payload != null ? that.payload.getClass() : null)) {
            return false;
        }
        return Objects.nonNull(tearDown) ? Objects.nonNull(that.tearDown) : Objects.isNull(that.tearDown);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "stateType=" + stateType.toString()
            + ", result=" + Strings.toString(this.result)
            + ", name='" + name + '\''
            + ')';
    }
}
