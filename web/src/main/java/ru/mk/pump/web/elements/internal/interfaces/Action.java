package ru.mk.pump.web.elements.internal.interfaces;

import java.util.Map;
import java.util.Set;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.SetState;

@SuppressWarnings("unused")
public interface Action<T> extends StrictInfo {

    T get();

    ActionStage getStage();

    void setStage(ActionStage currentStage);

    String name();

    InternalElement getTarget();

    Action<T> withParameters(Parameters parameters);

    Parameters getParameters();

    int getTry();

    SetState getRedefineState();

    Set<ActionStrategy> getStrategy();

    Action<T> redefineExpectedState(SetState stateSet);

    Action<T> withStrategy(ActionStrategy... strategies);

    enum ActionStage {
        NOT_RUN, BEFORE, MAIN, AFTER, FINALLY, COMPLETED
    }
}
