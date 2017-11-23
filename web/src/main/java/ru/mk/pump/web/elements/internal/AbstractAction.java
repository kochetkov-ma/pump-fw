package ru.mk.pump.web.elements.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

@SuppressWarnings({"unused", "WeakerAccess"})
abstract class AbstractAction<T> implements Action<T> {

    private static final int MAX_TRY = 5;

    private final Function<WebElement,T> actionSupplier;

    private final InternalElement internalElement;

    private final String name;

    private int actionExecutionTry = 0;

    private ActionStage currentStage;

    private Map<String, Parameter> parameters = new HashMap<>();

    AbstractAction(Function<WebElement,T> actionFunction, InternalElement internalElement, String name) {
        this.actionSupplier = actionFunction;
        this.internalElement = internalElement;
        this.name = name;
    }

    WebElement getInteractElement(){
        return internalElement.getFinder().findFast().throwExceptionOnFail().getResult();
    }

    @Override
    public T get() {
        RuntimeException ex = null;
        Error error = null;
        actionExecutionTry = 0;
        while (actionExecutionTry <= MAX_TRY) {
            try {
                actionExecutionTry++;
                return actionSupplier.apply(getInteractElement());
            } catch (RuntimeException ignore) {
                ex = ignore;
            } catch (Error ignore) {
                error = ignore;
            }
        }
        if (ex != null) {
            throw ex;
        }
        throw error;
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

    @Override
    public int getTry() {
        return actionExecutionTry;
    }
}
