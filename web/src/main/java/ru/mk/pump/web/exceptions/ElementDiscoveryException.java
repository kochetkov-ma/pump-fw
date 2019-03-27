package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;
import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.elements.api.Element;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@NoArgsConstructor
public class ElementDiscoveryException extends WebException {

    public ElementDiscoveryException(@Nullable String title, @Nullable ElementImplDispatcher dispatcher) {
        this(title, dispatcher, null);
    }

    public ElementDiscoveryException(@Nullable String title, @Nullable ElementImplDispatcher dispatcher, @Nullable Throwable cause) {
        super(title, cause);
        withMainDispatcher(dispatcher);
    }

    public ElementDiscoveryException withMainDispatcher(ElementImplDispatcher dispatcher) {
        return (ElementDiscoveryException)super.withDispatcher(dispatcher);
    }

    public ElementDiscoveryException withSecondDispatcher(ElementImplDispatcher dispatcher) {
        return (ElementDiscoveryException)super.withEnvDispatcher(dispatcher);
    }
}