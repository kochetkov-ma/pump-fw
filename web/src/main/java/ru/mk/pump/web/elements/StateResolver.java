package ru.mk.pump.web.elements;

import lombok.Getter;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.elements.internal.InternalElement;
import ru.mk.pump.web.exceptions.ElementException;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
public class StateResolver {

    @Getter
    private final InternalElement internalElement;

    public StateResolver(InternalElement internalElement) {
        this.internalElement = internalElement;
    }

    public MultiState resolve(MultiState state) {
        final long timeout = internalElement.getWaiter().getTemeoutS() * 1000;
        long syncTimeLeft = timeout;
        for (State item : state.get()) {
            resolve(item);
            final WaitResult<Boolean> res = item.result().orElseThrow(UnknownError::new);
            syncTimeLeft = syncTimeLeft - res.getElapsedTime();
            if (syncTimeLeft <= 0 || !res.isSuccess()) {
                throw new ElementException(
                    String.format("State not expected state '%s' in timeout '%s'", state.toString(), timeout),
                    internalElement,
                    res.getCause());
            }
        }
        return state.setResult(WaitResult.trueResult(timeout - syncTimeLeft, internalElement.getWaiter().getTemeoutS()));
    }

    public State resolve(State state) {
        return state.setResult(internalElement.getWaiter().wait(state.get()));
    }


}
