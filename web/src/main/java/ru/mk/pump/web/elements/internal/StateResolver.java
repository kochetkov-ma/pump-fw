package ru.mk.pump.web.elements.internal;

import static java.lang.String.format;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.elements.internal.State.StateType;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.BrowserException;
import ru.mk.pump.web.exceptions.ElementException;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
@Slf4j
public class StateResolver {

    @Getter
    private final InternalElement internalElement;

    public StateResolver(InternalElement internalElement) {
        this.internalElement = internalElement;
    }

    public SetState resolve(SetState state) {
        final SetState finalState = reorganizeIfNeed(state);
        final long timeout = internalElement.getWaiter().getTimeoutS() * 1000;
        long syncTimeLeft = timeout;
        for (AbstractState item : finalState.get()) {
            if (item instanceof State) {
                resolve((State) item);
            } else if (item instanceof OrState) {
                resolve((OrState) item);
            }
            @SuppressWarnings({"unchecked", "ConstantConditions"}) final WaitResult<Boolean> res = (WaitResult<Boolean>) item.result().get();
            syncTimeLeft = syncTimeLeft - res.getElapsedTime();
            if (syncTimeLeft <= 0 || !res.isSuccess()) {
                return (SetState) finalState.setResult(WaitResult.falseResult(timeout - syncTimeLeft, internalElement.getWaiter().getTimeoutS(), res.getCause())
                    .withExceptionOnFail(waitResult -> newResolvedException(finalState.name(), finalState.getInfo(), waitResult)));
            }
        }
        return (SetState) finalState.setResult(WaitResult.trueResult(timeout - syncTimeLeft, internalElement.getWaiter().getTimeoutS()));
    }

    OrState resolve(OrState state) {
        final WaitResult<Boolean> res = internalElement.getWaiter()
            .withDelay(50)
            .wait(() -> state.get().stream().anyMatch(s -> {
                try {
                    return s.call();
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    }
                    return false;
                }
            }))
            .withExceptionOnFail(waitResult -> newResolvedException(state.name(), state.getInfo(), waitResult));
        state.getTearDown().ifPresent(waitResultConsumer -> waitResultConsumer.accept(res));
        throwIfCause(res);
        return (OrState) state.setResult(res);
    }

    public State resolve(State state) {
        final WaitResult<Boolean> res = internalElement.getWaiter()
            .withDelay(50)
            .wait(state.get())
            .withExceptionOnFail(waitResult -> newResolvedException(state.name(), state.getInfo(), waitResult));
        state.getTearDown().ifPresent(waitResultConsumer -> waitResultConsumer.accept(res));
        throwIfCause(res);
        return (State) state.setResult(res);
    }

    protected PumpException newResolvedException(String state, Map<String, String> stateExceptionInfo, WaitResult<Boolean> waitResult) {
        return new ElementException(
            format("Element was not became to expected state '%s' in timeout '%s' sec", state, internalElement.getWaiter().getTimeoutS())
            , internalElement
            , (pumpMessage -> pumpMessage.addExtraInfo(stateExceptionInfo))
            , waitResult.getCause());

    }

    private static class OrState extends AbstractState<List<Callable<Boolean>>> {

        protected OrState(List<Callable<Boolean>> statePayload, StateType stateType) {
            super(statePayload, stateType);
        }

        @Override
        public List<Callable<Boolean>> get() {
            return getPayload();
        }

        public AbstractState<List<Callable<Boolean>>> withPayLoad(Callable<Boolean> payLoad) {
            getPayload().add(payLoad);
            return this;
        }
    }

    private void throwIfCause(WaitResult<Boolean> booleanWaitResult) {
        booleanWaitResult.ifHasCause(cause -> {
            if (cause instanceof BrowserException) {
                booleanWaitResult.throwExceptionOnFail();
            }
        });
    }

    private SetState reorganizeIfNeed(SetState state) {
        final List<State> andStates = Lists.newArrayList();
        final OrState notStates = (OrState) new OrState(Lists.newArrayList(), StateType.OR_STATUS_UNION).withName("");
        for (AbstractState item : state.get()) {
            if (item instanceof State) {
                if (item.type().isNot()) {
                    notStates.withPayLoad(((State) item).getPayload()).withName(notStates.name() + " OR " + item.name())
                        .withTearDown(((State) item).getTearDown().orElse(null));
                } else {
                    andStates.add((State) item);
                }
            }
        }
        if (notStates.getPayload().size() > 1) {
            notStates.withName(notStates.name().replaceFirst(" OR ", ""));
            return (SetState) SetState.of(state.type(), ImmutableSet.<AbstractState>builder().add(notStates).addAll(andStates).build()).withName(state.name());
        } else {
            return state;
        }
    }

}
