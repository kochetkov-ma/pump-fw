package ru.mk.pump.web.elements.internal;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.exceptions.ElementException;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;

import java.util.Map;

import static java.lang.String.format;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
@Slf4j
public class StateResolver {

    @Getter
    private final InternalElement internalElement;

    public StateResolver(InternalElement internalElement) {
        this.internalElement = internalElement;
    }

    public SetState resolve(SetState state) {
        final long timeout = internalElement.getWaiter().getTimeoutS() * 1000;
        long syncTimeLeft = timeout;
        for (State item : state.get()) {
            resolve(item);
            final WaitResult<Boolean> res = item.result().orElseThrow(UnknownError::new);
            syncTimeLeft = syncTimeLeft - res.getElapsedTime();
            if (syncTimeLeft <= 0 || !res.isSuccess()) {
                return (SetState) state.setResult(WaitResult.falseResult(timeout - syncTimeLeft, internalElement.getWaiter().getTimeoutS(), res.getCause())
                        .withExceptionOnFail(waitResult -> newResolvedException(state.name(), state.getInfo(), waitResult)));
            }
        }
        return (SetState) state.setResult(WaitResult.trueResult(timeout - syncTimeLeft, internalElement.getWaiter().getTimeoutS()));
    }

    public State resolve(State state) {
        try {
            if (state.get().call()) {
                log.debug("StateResolver.resolve resolve status {} from cache is success ", state.toString());
                return (State) state.setResult(WaitResult.trueResult(0, internalElement.getWaiter().getTimeoutS()));
            }
        } catch (NoSuchElementException | ElementFinderNotFoundException notIgnoreIfNOT) {
            internalElement.getFinder().setCache(null);
            if (state.type().isNot() && state.type() != State.StateType.EXISTS){
                return (State) state.setResult(WaitResult.trueResult(0, internalElement.getWaiter().getTimeoutS()));
            }
        } catch (Exception ignore) {
            log.debug("StateResolver.resolve cannot resolve status {} from cache {}", state.toString(), internalElement.toString());
            log.debug("StateResolver.resolve - Has exception", ignore);
            internalElement.getFinder().setCache(null);
        }
        final WaitResult<Boolean> res;
        if (state.type().isNot()){
            try {
                res = internalElement.getWaiter()
                        .withNotIgnoreExceptions(NoSuchElementException.class).withNotIgnoreExceptions(ElementFinderNotFoundException.class)
                        .wait(state.get())
                        .withExceptionOnFail(waitResult -> newResolvedException(state.name(), state.getInfo(), waitResult));
                state.getTearDown().ifPresent(waitResultConsumer -> waitResultConsumer.accept(res));
            } catch (NoSuchElementException | ElementFinderNotFoundException notIgnoreIfNOT) {
                return (State) state.setResult(WaitResult.trueResult(0, internalElement.getWaiter().getTimeoutS()));
            }
        } else {
            res = internalElement.getWaiter().wait(state.get())
                    .withExceptionOnFail(waitResult -> newResolvedException(state.name(), state.getInfo(), waitResult));
            state.getTearDown().ifPresent(waitResultConsumer -> waitResultConsumer.accept(res));
        }
        return (State) state.setResult(res);
    }

    protected PumpException newResolvedException(String state, Map<String, String> stateExceptionInfo, WaitResult<Boolean> waitResult) {
        return new ElementException(format("Element was not become to expected state '%s' in timeout '%s' sec", state, internalElement.getWaiter().getTimeoutS())
                , internalElement
                , (pumpMessage -> pumpMessage.addExtraInfo(stateExceptionInfo))
                , waitResult.getCause());

    }

}
