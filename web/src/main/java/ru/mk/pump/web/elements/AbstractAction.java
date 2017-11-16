package ru.mk.pump.web.elements;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.elements.internal.InternalElement;

@SuppressWarnings("unused")
abstract class AbstractAction<T> implements Action<T> {

    private static final int MAX_TRY = 5;

    private final Supplier<T> actionSupplier;

    private final InternalElement internalElement;

    private final String name;

    private int actionExecutionTry = 0;

    private ActionStage currentStage;

    private Map<String, Parameter> parameters = new HashMap<>();

    public AbstractAction(Supplier<T> actionSupplier, InternalElement internalElement, String name) {
        this.actionSupplier = actionSupplier;
        this.internalElement = internalElement;
        this.name = name;
    }

    @Override
    public T get() {
        RuntimeException ex = null;
        Error error = null;
        while (actionExecutionTry <= MAX_TRY) {
            try {
                actionExecutionTry++;
                return actionSupplier.get();
            } catch (RuntimeException ignore) {
                ex = ignore;
            } catch (Error ignore) {
                error = ignore;
            }
        }
        if (ex != null) {
            throw ex;
        }
        if (error != null) {
            throw error;
        }
        throw new UnknownError("Contact to the framework developer to fix this 'UnknownError'");
    }

    @Override
    public ActionStage getStage() {
        return currentStage;
    }

    @Override
    public void setStage(ActionStage currentStage) {
        this.currentStage = currentStage;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public InternalElement getTarget() {
        return internalElement;
    }

    @Override
    public Action<T> withParameters(Map<String, Parameter> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    @Override
    public Map<String, Parameter> getParameters() {
        return parameters;
    }
}
