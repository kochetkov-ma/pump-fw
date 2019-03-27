package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

import javax.annotation.Nullable;

@SuppressWarnings({"unused"})
@NoArgsConstructor
public class ElementStateException extends ElementException {

    public ElementStateException(@Nullable String title, @Nullable State state) {
        this(title, state, null, null);
    }

    public ElementStateException(@Nullable String title, @Nullable State state,  @Nullable InternalElement internalElement, @Nullable Throwable cause) {
        super();
        withTitle(title);
        withState(state);
        withInternalElement(internalElement);
        withCause(cause);
    }

    @Override
    public ElementStateException withState(@Nullable State state) {
        return (ElementStateException) super.withState(state);
    }
}
