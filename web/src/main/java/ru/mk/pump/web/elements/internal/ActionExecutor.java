package ru.mk.pump.web.elements.internal;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.elements.api.listeners.ActionListener;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.Action.ActionStage;
import ru.mk.pump.web.exceptions.ActionExecutingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"WeakerAccess", "unused"})
@Slf4j
public class ActionExecutor extends ActionNotifier {

    private static final int MAX_TRY = 5;

    @Getter
    private final Parameters parameters = Parameters.of();

    private final List<Action> beforeActions = new ArrayList<>();

    private final List<Action> afterActions = new ArrayList<>();

    private final List<Action> afterActionError = new ArrayList<>();

    @Getter
    private final boolean main;

    private int actionExecutionTry = 0;
    @Getter
    private StateResolver stateResolver;

    public ActionExecutor() {
        this(Sets.newHashSet(), true);
    }

    protected ActionExecutor(Set<ActionListener> actionListeners) {
        this(actionListeners, true);
    }

    protected ActionExecutor(Set<ActionListener> actionListeners, boolean main) {
        super(actionListeners);
        this.main = main;
    }

    public ActionExecutor withStateResolver(StateResolver stateResolver) {
        this.stateResolver = stateResolver;
        return this;
    }

    public ActionExecutor clearAllActions() {
        beforeActions.clear();
        afterActions.clear();
        afterActionError.clear();
        return this;
    }

    public ActionExecutor addBefore(Action beforeAction) {
        this.beforeActions.add(beforeAction);
        return this;
    }

    public ActionExecutor addAfter(Action afterAction) {
        this.afterActions.add(afterAction);
        return this;
    }

    public ActionExecutor addAfterError(Action afterActionError) {
        this.afterActionError.add(afterActionError);
        return this;
    }

    public <T> T execute(Action<T> tAction) {
        tAction.setStage(ActionStage.NOT_RUN);
        try {
            if (isExcludedStrategy(tAction, ActionStrategy.NO_STATE_CHECK)) {
                if (tAction.getRedefineState() != null) {
                    tAction.setStage(ActionStage.BEFORE);
                    stateResolver.resolve(tAction.getRedefineState()).result().throwExceptionOnFail();
                } else {
                    if (stateResolver != null) {
                        tAction.setStage(ActionStage.BEFORE);
                        stateResolver.resolve(tAction.getTarget().ready()).result().throwExceptionOnFail();
                    }
                }
            }
            if (main && ConfigurationHolder.get().getElement().isElementHighlight()) {
                tAction.getTarget().highlight(true);
            }
        } catch (Throwable throwable) {
            notifyOnFail(tAction, throwable);
            throw new ActionExecutingException("Action executing error", tAction, throwable);
        } finally {
            if (!afterActionError.isEmpty() && isExcludedStrategy(tAction, ActionStrategy.SIMPLE, ActionStrategy.NO_FINALLY)) {
                tAction.setStage(ActionStage.FINALLY);
                final ActionExecutor helperExecutor = new ActionExecutor(getActionListeners(), false).withParameters(parameters);
                afterActionError.forEach(helperExecutor::payloadExecute);
                notifyOnFinallyStateCheck(tAction);
            }
            actionExecutionTry = 0;
        }
        return payloadExecute(tAction);
    }

    public ActionExecutor withParameters(Parameters parameters) {
        this.parameters.addAll(parameters);
        return this;
    }

    protected boolean needNewExecution(Action tAction, Throwable throwable) {
        if (throwable instanceof AssertionError || throwable instanceof ActionExecutingException || actionExecutionTry >= MAX_TRY) {
            return false;
        }
        if (throwable instanceof NoSuchElementException || throwable instanceof InvalidElementStateException) {
            return true;
        }
        return true;
    }

    protected <T> T payloadExecute(Action<T> tAction) {
        T result = null;
        tAction.withParameters(parameters);
        ActionExecutor helperExecutor;
        actionExecutionTry++;
        try {
            if (!beforeActions.isEmpty() && isExcludedStrategy(tAction, ActionStrategy.SIMPLE, ActionStrategy.NO_BEFORE)) {
                tAction.setStage(ActionStage.BEFORE);
                helperExecutor = new ActionExecutor(getActionListeners(), false).withParameters(parameters);
                beforeActions.forEach(helperExecutor::payloadExecute);
                notifyOnBeforeSuccess(tAction);
            }
            tAction.setStage(ActionStage.MAIN);
            result = tAction.get();

            if (!afterActions.isEmpty() && isExcludedStrategy(tAction, ActionStrategy.SIMPLE, ActionStrategy.NO_AFTER)) {
                tAction.setStage(ActionStage.AFTER);
                helperExecutor = new ActionExecutor(getActionListeners(), false).withParameters(parameters);
                afterActions.forEach(helperExecutor::payloadExecute);
            }

            tAction.setStage(ActionStage.COMPLETED);
        } catch (Throwable throwable) {
            if (needNewExecution(tAction, throwable)) {
                actionExecutionTry++;
                return payloadExecute(tAction);
            }

            if (tAction.getStage() == ActionStage.AFTER) {
                log.error("Error when execute after actions", throwable);
                notifyOnAfterActionFail(tAction);
                tAction.setStage(ActionStage.COMPLETED);
            } else {
                notifyOnFail(tAction, throwable);
                if (main && ConfigurationHolder.get().getElement().isElementHighlight()) {
                    tAction.getTarget().highlight(false);
                }
                throw new ActionExecutingException("Main action executing was failed", tAction, throwable);
            }
        }
        notifyOnSuccess(tAction, result);
        if (main && ConfigurationHolder.get().getElement().isElementHighlight()) {
            tAction.getTarget().highlight(false);
        }
        return result;
    }

    private boolean isExcludedStrategy(Action<?> action, ActionStrategy... strategies) {
        return action.getStrategy().isEmpty() || action.getStrategy().stream().anyMatch(i -> i.equals(ActionStrategy.STANDARD)) || Arrays.stream(strategies)
                .noneMatch(item -> action.getStrategy().stream().anyMatch(strategy -> strategy.equals(item)));
    }
}
