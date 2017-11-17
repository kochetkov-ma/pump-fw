package ru.mk.pump.web.elements;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.elements.State.StateType;

@SuppressWarnings("unused")
public class MultiState {

    private final StateType stateType;

    private final List<State> states;

    private WaitResult<Boolean> result;

    private String name = "";

    private MultiState(List<State> states, StateType stateType) {
        this.states = ImmutableList.copyOf(states);
        this.stateType = stateType;
    }

    public static MultiState of(StateType stateType, State... states) {
        return new MultiState(Arrays.asList(states), stateType);
    }

    @SafeVarargs
    public static MultiState of(StateType stateType, List<State>... states) {
        final List<State> res = new ArrayList<>();
        Arrays.stream(states).forEach(res::addAll);
        return new MultiState(res, stateType);
    }

    public boolean isResolved() {
        return result != null;
    }

    public Optional<WaitResult<Boolean>> result() {
        return Optional.ofNullable(result);
    }

    public MultiState setResult(@Nullable WaitResult<Boolean> result) {
        this.result = result;
        return this;
    }

    public List<State> get() {
        return states;
    }

    public MultiState withName(String name) {
        this.name = name;
        return this;
    }

    public String name() {
        return name;
    }

    public StateType type() {
        return stateType;
    }
}
