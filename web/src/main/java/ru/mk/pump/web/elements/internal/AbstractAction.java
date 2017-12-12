package ru.mk.pump.web.elements.internal;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

@SuppressWarnings({"unused", "WeakerAccess"})
abstract class AbstractAction<T> implements Action<T> {

    private static final int MAX_TRY = 5;

    private final BiFunction<WebElement, Map<String, Parameter<?>>, T> actionSupplier;

    private final InternalElement internalElement;

    private final String name;

    private int actionExecutionTry = 0;

    private ActionStage currentStage;

    private Set<ActionStrategy> actionStrategies = Sets.newHashSet();

    private Map<String, Parameter<?>> parameters = new HashMap<>();

    private SetState stateSet;

    AbstractAction(BiFunction<WebElement, Map<String, Parameter<?>>, T> actionFunction, InternalElement internalElement, String name) {
        this.actionSupplier = actionFunction;
        this.internalElement = internalElement;
        this.name = name;
    }

    WebElement getInteractElement() {
        return internalElement.getFinder().findFast().throwExceptionOnFail().getResult();
    }

    @Override
    public T get() {
        RuntimeException ex = null;
        Error error = null;
        actionExecutionTry = 0;
        while (actionExecutionTry < MAX_TRY) {
            try {
                actionExecutionTry++;
                return actionSupplier.apply(getInteractElement(), getParameters());
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

    public ActionStage getStage() {
        return currentStage;
    }

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
    public Action<T> withParameters(Map<String, Parameter<?>> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    @Override
    public Map<String, Parameter<?>> getParameters() {
        return parameters;
    }

    @Override
    public int getTry() {
        return actionExecutionTry;
    }

    @Override
    public SetState getRedefineState() {
        return stateSet;
    }

    @Override
    public Set<ActionStrategy> getStrategy() {
        return actionStrategies;
    }

    @Override
    public Action<T> withStrategy(ActionStrategy... strategies) {
        actionStrategies.addAll(Arrays.asList(strategies));
        return this;
    }

    @Override
    public Action<T> redefineExpectedState(SetState stateSet) {
        this.stateSet = stateSet;
        return this;
    }

    @Override
    public Map<String, String> getInfo() {
        final LinkedHashMap<String, String> result = new LinkedHashMap<>();
        result.put("type", this.name);
        result.put("try", String.valueOf(this.actionExecutionTry));
        result.put("action strategies", Strings.toString(this.actionStrategies));
        result.put("current stage", this.currentStage.toString());
        result.put("target element", Strings.space(internalElement.getName(), internalElement.getBy().toString()));
        result.put("parameters", this.parameters.toString());
        result.put("max tries", String.valueOf(MAX_TRY));
        if (stateSet != null) {
            result.put("redefined stateSet", this.stateSet.toString());
        }
        return result;
    }
}
