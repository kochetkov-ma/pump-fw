package ru.mk.pump.web.elements.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.elements.internal.interfaces.InternalState;

@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
abstract class AbstractState<T> implements InternalState<T> {

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

    @Override
    abstract public T get();

    public State.StateType type() {
        return stateType;
    }

    public String name() {
        return name;
    }

    public AbstractState<T> withName(String name) {
        this.name = name;
        return this;
    }

    public Optional<Consumer<WaitResult<Boolean>>> getTearDown() {
        return Optional.ofNullable(tearDown);
    }

    public AbstractState<T> withTearDown(Consumer<WaitResult<Boolean>> tearDown) {
        this.tearDown = tearDown;
        return this;
    }

    public AbstractState<T> setResult(@Nullable WaitResult<Boolean> result) {
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
        result.put("result", Objects.toString(result));
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

        AbstractState<?> that = (AbstractState<?>) o;

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
            + ", result=" + Objects.toString(result)
            + ", name='" + name + '\''
            + ')';
    }
}
