package ru.mk.pump.web.elements.internal.interfaces;

import java.util.Optional;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.elements.internal.State;

public interface InternalState<T> extends StrictInfo {

    T get();

    State.StateType type();

    String name();

    InternalState<T> withName(String name);

    Optional<Consumer<WaitResult<Boolean>>> getTearDown();

    InternalState<T> withTearDown(Consumer<WaitResult<Boolean>> tearDown);

    InternalState<T> setResult(@Nullable WaitResult<Boolean> result);

    WaitResult<Boolean> result();

    boolean isResolved();

}
