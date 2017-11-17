package ru.mk.pump.web.elements;


import java.util.Optional;
import java.util.concurrent.Callable;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.utils.Waiter.WaitResult;

@SuppressWarnings("unused")
@ToString(of = {"stateType", "name", "result"})
public class State {

    private final Callable<Boolean> callable;

    private final StateType stateType;

    private WaitResult<Boolean> result;

    private String name = "";

    private State(Callable<Boolean> callable, StateType stateType) {
        this.callable = callable;
        this.stateType = stateType;
    }

    public static State of(Callable<Boolean> callable, StateType stateType) {
        return new State(callable, stateType);
    }

    public boolean isResolved() {
        return result != null;
    }

    public Optional<WaitResult<Boolean>> result() {
        return Optional.ofNullable(result);
    }

    public State setResult(@Nullable WaitResult<Boolean> result) {
        this.result = result;
        return this;
    }

    public Callable<Boolean> get() {
        return callable;
    }

    public State withName(String name) {
        this.name = name;
        return this;
    }

    public String name() {
        return name;
    }

    public StateType type() {
        return stateType;
    }

    public enum StateType {
        EXISTS, READY, OTHER, MULTI_FINAL
    }
}
