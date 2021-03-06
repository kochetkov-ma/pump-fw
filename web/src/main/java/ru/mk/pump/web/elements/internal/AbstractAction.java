package ru.mk.pump.web.elements.internal;

import com.google.common.collect.Sets;
import lombok.ToString;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

import java.util.*;
import java.util.function.BiFunction;

@SuppressWarnings({"unused", "WeakerAccess"})
@ToString(exclude = {"actionSupplier", "stateSet"})
abstract class AbstractAction<T> implements Action<T> {

    private static final int MAX_TRY = 5;

    private int maxTruCount = MAX_TRY;

    private final BiFunction<WebElement, Parameters, T> actionSupplier;

    private final InternalElement internalElement;

    private final String name;

    private int actionExecutionTry = 0;

    private ActionStage currentStage;

    private Set<ActionStrategy> actionStrategies = Sets.newHashSet();

    private Parameters parameters = Parameters.of();

    private SetState stateSet;

    AbstractAction(BiFunction<WebElement, Parameters, T> actionFunction, InternalElement internalElement, String name) {
        this.actionSupplier = actionFunction;
        this.internalElement = internalElement;
        this.name = name;
    }

    @Override
    public T get() {
        RuntimeException ex = null;
        Error error = null;
        actionExecutionTry = 0;
        while (actionExecutionTry < maxTruCount) {
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
    public Action<T> withParameters(Parameters parameters) {
        this.parameters.addAll(parameters);
        return this;
    }

    @Override
    public Parameters getParameters() {
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
    public Action<T> redefineExpectedState(SetState stateSet) {
        this.stateSet = stateSet;
        return this;
    }

    public AbstractAction<T> setMaxTruCount(int maxTruCount) {
        this.maxTruCount = maxTruCount;
        return this;
    }

    @Override
    public Action<T> withStrategy(ActionStrategy... strategies) {
        actionStrategies.addAll(Arrays.asList(strategies));
        return this;
    }

    @Override
    public Map<String, String> getInfo() {
        final LinkedHashMap<String, String> result = new LinkedHashMap<>();
        result.put("type", this.name);
        result.put("try", String.valueOf(this.actionExecutionTry));
        result.put("action strategies", Str.toString(this.actionStrategies));
        result.put("current stage", this.currentStage.toString());
        result.put("target element", Str.space(internalElement.getName(), internalElement.getBy().toString()));
        result.put("parameters", this.parameters.toString());
        result.put("max tries", String.valueOf(maxTruCount));
        if (stateSet != null) {
            result.put("redefined stateSet", this.stateSet.toString());
        }
        return result;
    }

    WebElement getInteractElement() {

        return internalElement.getFinder().findFast().throwExceptionOnFail().getResult();
    }
}
