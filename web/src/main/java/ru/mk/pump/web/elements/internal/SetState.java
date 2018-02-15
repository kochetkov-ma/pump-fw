package ru.mk.pump.web.elements.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.elements.enums.StateType;

@SuppressWarnings("unused")
public class SetState extends State {

    @Getter
    @NonNull
    private final Set<State> stateSet = Sets.newLinkedHashSet();

    @Getter
    private final State stateOr;

    private SetState(@NonNull StateType stateType, @NonNull Set<State> states, @Nullable Set<State> statesOr,
        @Nullable Consumer<WaitResult<Boolean>> tearDown) {
        super(stateType, Collections.emptySet(), tearDown);
        states.forEach(i -> {
            if (i instanceof SetState) {
                stateSet.addAll(((SetState) i).getStateSet());
            } else {
                stateSet.add(i);
            }
        });
        if (statesOr != null && !statesOr.isEmpty()) {
            stateOr = State.of(StateType.OR_STATUS_UNION, Collections.emptySet());
            stateOr.withName("");
            toStateOr(statesOr);
        } else {
            stateOr = null;
        }
    }

    public static SetState of(@NonNull StateType stateType, @NonNull State... states) {
        if (states.length == 0) {
            throw new IllegalArgumentException("States varargs cannot be nonArg or null");
        }
        final Set<State> and = Sets.newLinkedHashSet();
        final Set<State> or = Sets.newLinkedHashSet();
        Arrays.stream(states).forEach(item -> {
            if (item.type().isOr()) {
                or.add(item);
            } else {
                and.add(item);
            }
        });
        return new SetState(stateType, and, or, null);
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoBuilder("SetState")
            .put("state type", type().name())
            .put("state name", name())
            .put("result", Objects.toString(result()))
            .put("states", Strings.toPrettyString(getAll()))
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
        sb.append(", states=").append(Strings.toPrettyString(getAll()));
        sb.append(')');
        return sb.toString();
    }

    private void toStateOr(Set<State> statesOrSet) {
        if (stateOr == null) {
            return;
        }
        for (State item : statesOrSet) {
            if (item instanceof SetState && ((SetState) item).getStateOr() != null) {
                stateOr.withPayload(Preconditions.checkNotNull(((SetState) item).getStateOr().get()));
            } else {
                stateOr.withPayload(item.get());
            }
            stateOr.withName(stateOr.name() + " OR " + item.name())
                .withTearDown(item.getTearDown().orElse(null));
        }
        if (statesOrSet.size() > 1) {
            stateOr.withName(stateOr.name().replaceFirst(" OR ", ""));
        }
    }

    private Set<State> getAll() {
        final ImmutableSet.Builder<State> builder = ImmutableSet.<State>builder().addAll(stateSet);
        if (stateOr != null) {
            builder.add(stateOr);
        }
        return builder.build();
    }
}
