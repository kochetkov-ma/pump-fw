package ru.mk.pump.web.elements.internal;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.BrowserException;
import ru.mk.pump.web.exceptions.ElementStateException;

import java.util.concurrent.Callable;

import static ru.mk.pump.commons.utils.Str.format;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
@Slf4j
public class StateResolver extends StateNotifier {

    @Getter
    private final InternalElement internalElement;

    public StateResolver(InternalElement internalElement) {
        this.internalElement = internalElement;
    }

    public State resolveFast(State state) {
        return resolve(state, 500);
    }

    public State resolve(State state, ElementWaiter waiter) {
        final StateResolver resolver = new StateResolver(internalElement) {
            @Override
            protected ElementWaiter waiter() {
                return waiter;
            }
        };
        if (state instanceof SetState) {
            return resolver.resolve((SetState) state);
        } else {
            return resolver.resolve(state);
        }
    }

    public State resolve(State state, int timeoutMs, int delayMs) {
        return resolve(state, waiter().withTimeoutMs(timeoutMs).withDelay(delayMs));
    }

    public State resolve(State state, int timeoutMs) {
        return resolve(state, waiter().withTimeoutMs(timeoutMs));
    }

    public State resolve(State state) {
        return resolveAnd(state);
    }

    public SetState resolve(SetState state) {
        notifyOnBefore(Pair.of(state, internalElement));
        final long timeout = waiter().getTimeout() * 1000;
        long syncTimeLeft = timeout;
        WaitResult<Boolean> result = WaitResult.trueResult(0, waiter().getTimeout());
        if (state.getStateOr() != null && !state.getStateOr().get().isEmpty()) {
            result = resolveOr(state.getStateOr()).result();
            syncTimeLeft = syncTimeLeft - result.getElapsedTime();
        }
        if (!state.getStateSet().isEmpty() && result.isSuccess()) {
            for (State item : state.getStateSet()) {
                resolveAnd(item);
                result = item.result();
                syncTimeLeft = syncTimeLeft - result.getElapsedTime();
                if (syncTimeLeft <= 0 || !result.isSuccess()) {
                    state.setResult(WaitResult.falseResult(timeout - syncTimeLeft, waiter().getTimeout(), result.getCause())
                            .withExceptionOnFail(waitResult -> newResolvedException(state, waitResult)));
                    notifyOnFinish(Pair.of(state, internalElement));
                    return state;
                }
            }
        } else {
            if (!result.isSuccess()) {
                state.setResult(WaitResult.falseResult(result.getElapsedTime(), waiter().getTimeout(), result.getCause())
                        .withExceptionOnFail(waitResult -> newResolvedException(state, waitResult)));
                notifyOnFinish(Pair.of(state, internalElement));
                return state;
            }
        }
        state.setResult(WaitResult.trueResult(timeout - syncTimeLeft, waiter().getTimeout()));
        notifyOnFinish(Pair.of(state, internalElement));
        return state;

    }

    State resolveOr(State state) {
        final WaitResult<Boolean> res = waiter()
                .withDelay(ElementWaiter.DEFAULT_DELAY_MS)
                .wait(() -> state.get().stream().anyMatch(this::call))
                .withExceptionOnFail(waitResult -> newResolvedException(state, waitResult));
        state.getTearDown().ifPresent(waitResultConsumer -> waitResultConsumer.accept(res));
        throwIfCause(res);
        return state.setResult(res);
    }

    State resolveAnd(State state) {
        final WaitResult<Boolean> res = waiter()
                .withDelay(ElementWaiter.DEFAULT_DELAY_MS)
                .wait(() -> state.get().stream().allMatch(this::call))
                .withExceptionOnFail(waitResult -> newResolvedException(state, waitResult));
        state.getTearDown().ifPresent(waitResultConsumer -> waitResultConsumer.accept(res));
        throwIfCause(res);
        return state.setResult(res);
    }

    protected ElementStateException newResolvedException(State state, WaitResult<Boolean> waitResult) {
        return new ElementStateException(
                format("PElement was not became to expected state '%s' in timeout '%s' sec", state.name(), waiter().getTimeout()),
                state,
                internalElement,
                waitResult.getCause()
        );
    }

    protected ElementWaiter waiter() {
        return internalElement.getWaiter();
    }

    private boolean call(Callable<Boolean> s) {
        try {
            return s.call();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            return false;
        }
    }

    private void throwIfCause(WaitResult<Boolean> booleanWaitResult) {
        booleanWaitResult.ifHasCause(cause -> {
            if (cause instanceof BrowserException) {
                booleanWaitResult.throwExceptionOnFail();
            }
        });
    }
}