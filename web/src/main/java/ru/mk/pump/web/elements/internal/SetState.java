package ru.mk.pump.web.elements.internal;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.elements.internal.State.StateType;

@SuppressWarnings("unused")
public class SetState extends AbstractState<Set<AbstractState>> {

    private SetState(Set<AbstractState> states, StateType stateType) {

        super(ImmutableSet.copyOf(states), stateType);
    }

    public static SetState of(StateType stateType, AbstractState... states) {
        return new SetState(Sets.newLinkedHashSet(Arrays.asList(states)), stateType);
    }

    @SafeVarargs
    public static SetState of(StateType stateType, Set<AbstractState>... states) {
        final Set<AbstractState> res = new LinkedHashSet<>();
        Arrays.stream(states).forEach(res::addAll);
        return new SetState(res, stateType);
    }

    @Override
    public Set<AbstractState> get() {
        return getPayload();
    }

    @Override
    public Map<String, String> getInfo() {
        return ImmutableMap.<String, String>builder().putAll(super.getInfo()).put("states", Strings.toPrettyString(getPayload(), "states".length() + 3))
            .build();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SetState(");
        sb.append("stateType=").append(type().name());
        sb.append(", result=").append(Objects.toString(result()));
        sb.append(", name='").append(name());
        if (getTearDown().isPresent()) {
            sb.append(", tearDown=").append("exists");
        } else {
            sb.append(", tearDown=").append("empty");
        }
        sb.append(", states=").append(Strings.toPrettyString(getPayload()));
        sb.append(')');
        return sb.toString();
    }
}
