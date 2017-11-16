package ru.mk.pump.web.elements;

import java.util.concurrent.Callable;
import lombok.Getter;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.elements.internal.InternalElement;

public class StateResolver {

    @Getter
    private final InternalElement internalElement;

    public StateResolver(InternalElement internalElement){

        this.internalElement = internalElement;
    }

    WaitResult<Boolean> resolve(State state){
        internalElement.getWaiter().waitIgnoreExceptions()
    }
}
