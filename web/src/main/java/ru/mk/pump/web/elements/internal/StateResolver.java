package ru.mk.pump.web.elements.internal;

import static java.lang.String.format;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.elements.internal.State.StateType;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalState;
import ru.mk.pump.web.exceptions.BrowserException;
import ru.mk.pump.web.exceptions.ElementStateException;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
@Slf4j
public class StateResolver extends StateNotifier {

    @Getter
    private final InternalElement internalElement;

    private boolean fast = false;

    public StateResolver(InternalElement internalElement) {
        this.internalElement = internalElement;
    }


    public <T extends InternalState<?>> T resolveFast(T state) {
        fast = true;
        try {
            return resolve(state);
        } finally {

            fast = false;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends InternalState<?>> T resolve(T state) {
        notifyOnBefore(state);
        final InternalState<?> finalState = reorganizeIfNeed(state);
        final long timeout = waiter().getTimeoutS() * 1000;
        long syncTimeLeft = timeout;
        if (state instanceof State) {
            final T res = (T) resolve((State) state);
            notifyOnBefore(res);
            return res;
        } else if (state instanceof SetState) {
            for (AbstractState<?> item : ((SetState) finalState).get()) {
                if (item instanceof State) {
                    resolve((State) item);
                } else if (item instanceof OrState) {
                    resolve((OrState) item);
                } else if (item instanceof SetState) {
                    resolve((SetState) item);
                } else {
                    throw new UnsupportedOperationException(String.format("Unsupported state type '%s'", state.getClass().getSimpleName()));
                }
                final WaitResult<Boolean> res = item.result();
                syncTimeLeft = syncTimeLeft - res.getElapsedTime();
                if (syncTimeLeft <= 0 || !res.isSuccess()) {
                    finalState
                        .setResult(WaitResult.falseResult(timeout - syncTimeLeft, waiter().getTimeoutS(), res.getCause())
                            .withExceptionOnFail(waitResult -> newResolvedException(finalState, waitResult)));
                    notifyOnBefore(finalState);
                    return (T) finalState;
                }
            }
            finalState.setResult(WaitResult.trueResult(timeout - syncTimeLeft, waiter().getTimeoutS()));
            notifyOnBefore(finalState);
            return (T) finalState;
        }
        throw new UnsupportedOperationException(String.format("Unsupported state type '%s'", state.getClass().getSimpleName()));
    }

    OrState resolve(OrState state) {
        final WaitResult<Boolean> res = waiter()
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
            .withExceptionOnFail(waitResult -> newResolvedException(state, waitResult));
        state.getTearDown().ifPresent(waitResultConsumer -> waitResultConsumer.accept(res));
        throwIfCause(res);
        return (OrState) state.setResult(res);
    }

    State resolve(State state) {
        final WaitResult<Boolean> res = waiter()
            .withDelay(50)
            .wait(state.get())
            .withExceptionOnFail(waitResult -> newResolvedException(state, waitResult));
        state.getTearDown().ifPresent(waitResultConsumer -> waitResultConsumer.accept(res));
        throwIfCause(res);
        return (State) state.setResult(res);
    }

    protected PumpException newResolvedException(InternalState<?> state, WaitResult<Boolean> waitResult) {
        return new ElementStateException(format("Element was not became to expected state '%s' in timeout '%s' sec", state.name(), waiter().getTimeoutS()),
            waitResult.getCause())
            .withTargetState(state)
            .withElement(internalElement);
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

    private ElementWaiter waiter() {
        if (fast) {
            return ElementWaiter.newWaiterMs(500);
        } else {
            return internalElement.getWaiter();
        }
    }

    private void throwIfCause(WaitResult<Boolean> booleanWaitResult) {
        booleanWaitResult.ifHasCause(cause -> {
            if (cause instanceof BrowserException) {
                booleanWaitResult.throwExceptionOnFail();
            }
        });
    }

    private InternalState<?> reorganizeIfNeed(InternalState<?> state) {
        if (state instanceof SetState) {
            final List<State> andStates = Lists.newArrayList();
            final OrState notStates = (OrState) new OrState(Lists.newArrayList(), StateType.OR_STATUS_UNION).withName("");
            for (AbstractState item : ((SetState) state).get()) {
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
                return SetState.of(state.type(), ImmutableSet.<AbstractState>builder().add(notStates).addAll(andStates).build())
                    .withName(state.name());
            } else {
                return state;
            }
        } else {
            return state;
        }
    }

}