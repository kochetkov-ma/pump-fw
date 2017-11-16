package ru.mk.pump.web.elements;

import java.util.Map;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.elements.internal.InternalElement;

@SuppressWarnings("unused")
public interface Action<T> {

    T get();

    ActionStage getStage();

    void setStage(ActionStage currentStage);

    String name();

    InternalElement getTarget();

    Action<T> withParameters(Map<String, Parameter> parameters);

    Map<String, Parameter> getParameters();

    int getTry();

    enum ActionStage {
        NOT_RUN, BEFORE, MAIN, AFTER, FINALLY, COMPLETED;
    }
}
