package ru.mk.pump.web.elements.internal;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Waiter.WaitResult;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "WeakerAccess"})
abstract class AbstractState<T> implements StrictInfo {

    private final State.StateType stateType;

    private WaitResult<Boolean> result;

    private String name = "empty";

    @Getter(AccessLevel.PROTECTED)
    private T payload;

    private Consumer<WaitResult<Boolean>> tearDown = null;

    protected AbstractState(T statePayload, State.StateType stateType) {
        this(statePayload, stateType, null);
    }

    protected AbstractState(T statePayload, State.StateType stateType, Consumer<WaitResult<Boolean>> tearDown) {
        this.stateType = stateType;
        this.name = stateType.toString();
        this.payload = statePayload;
        this.tearDown = tearDown;
    }

    public boolean isResolved() {
        return result != null;
    }

    public Optional<WaitResult<Boolean>> result() {
        return Optional.ofNullable(result);
    }

    public AbstractState<T> setResult(@Nullable WaitResult<Boolean> result) {
        this.result = result;
        return this;
    }

    public Optional<Consumer<WaitResult<Boolean>>> getTearDown() {
        return Optional.ofNullable(tearDown);
    }

    abstract public T get();

    public AbstractState<T> withName(String name) {
        this.name = name;
        return this;
    }

    public String name() {
        return name;
    }

    public State.StateType type() {
        return stateType;
    }

    @Override
    public Map<String, String> getInfo() {
        return ImmutableMap.of("stateType", stateType.toString(), "result", Objects.toString(result), "name", name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "(");
        sb.append("stateType=").append(stateType.toString());
        sb.append(", result=").append(Objects.toString(result));
        sb.append(", name='").append(name).append('\'');
        if (getTearDown().isPresent()) {
            sb.append(", tearDown=").append("exists");
        } else {
            sb.append(", tearDown=").append("empty");
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractState<?> that = (AbstractState<?>) o;

        if (stateType != that.stateType) return false;
        if (!Objects.equals(result != null ? result.getResult() : null, that.result != null ? that.result.getResult() : null))
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        //noinspection SimplifiableIfStatement
        if (!Objects.equals(payload != null ? payload.getClass() : null, that.payload != null ? that.payload.getClass() : null))
            return false;
        return Objects.nonNull(tearDown) ? Objects.nonNull(that.tearDown) : Objects.isNull(that.tearDown);
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
}
