package ru.mk.pump.web.elements.internal;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import ru.mk.pump.web.elements.api.listeners.ActionListener;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.Action.ActionStage;
import ru.mk.pump.web.exceptions.ActionExecutingException;

@SuppressWarnings({"WeakerAccess", "unused"})
@NoArgsConstructor
@Slf4j
public class ActionExecutor extends ActionNotifier {

    private static final int MAX_TRY = 5;

    private final List<Action> beforeActions = new ArrayList<>();

    private final List<Action> afterActions = new ArrayList<>();

    private final List<Action> afterActionError = new ArrayList<>();

    private int actionExecutionTry = 0;

    private StateResolver stateResolver;

    protected ActionExecutor(Set<ActionListener> actionListeners) {
        super(actionListeners);
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
            if (tAction.getRedefineState() != null) {
                tAction.setStage(ActionStage.BEFORE);
                stateResolver.resolve(tAction.getRedefineState()).result().throwExceptionOnFail();
            } else {
                if (stateResolver != null) {
                    tAction.setStage(ActionStage.BEFORE);
                    stateResolver.resolve(tAction.getTarget().ready()).result().throwExceptionOnFail();
                }
            }
        } catch (Throwable throwable) {
            notifyOnFail(tAction, throwable);
            throw new ActionExecutingException(tAction, throwable);
        } finally {
            if (!afterActionError.isEmpty() && isRightStrategy(tAction, ActionStrategy.SIMPLE, ActionStrategy.NO_FINALLY)) {
                tAction.setStage(ActionStage.FINALLY);
                final ActionExecutor helperExecutor = new ActionExecutor(getActionListeners());
                afterActionError.forEach(helperExecutor::payloadExecute);
                notifyOnFinallyAfter(tAction);
            }
            actionExecutionTry = 0;
        }
        return payloadExecute(tAction);
    }

    protected boolean needNewExecution(Action tAction, Throwable throwable) {
        if (throwable instanceof AssertionError || actionExecutionTry == MAX_TRY) {
            return false;
        }
        if (throwable instanceof NoSuchElementException || throwable instanceof InvalidElementStateException) {
            return true;
        }
        return true;
    }

    protected <T> T payloadExecute(Action<T> tAction) {
        T result = null;
        ActionExecutor helperExecutor;
        actionExecutionTry++;
        try {
            if (!beforeActions.isEmpty() && isRightStrategy(tAction, ActionStrategy.SIMPLE, ActionStrategy.NO_BEFORE)) {
                tAction.setStage(ActionStage.BEFORE);
                helperExecutor = new ActionExecutor(getActionListeners());
                beforeActions.forEach(helperExecutor::payloadExecute);
            }

            tAction.setStage(ActionStage.MAIN);
            result = tAction.get();

            if (!afterActions.isEmpty() && isRightStrategy(tAction, ActionStrategy.SIMPLE, ActionStrategy.NO_AFTER)) {
                tAction.setStage(ActionStage.AFTER);
                helperExecutor = new ActionExecutor(getActionListeners());
                afterActions.forEach(helperExecutor::payloadExecute);
                notifyOnAfter(tAction);
            }

            tAction.setStage(ActionStage.COMPLETED);
        } catch (Throwable throwable) {
            if (needNewExecution(tAction, throwable)) {
                actionExecutionTry++;
                return payloadExecute(tAction);
            }

            if (tAction.getStage() == ActionStage.AFTER) {
                log.error("Error when execute after actions", throwable);
                tAction.setStage(ActionStage.COMPLETED);
            } else {
                notifyOnFail(tAction, throwable);
                throw new ActionExecutingException(tAction, throwable);
            }
        }
        notifyOnSuccess(tAction, result);
        return result;
    }

    private boolean isRightStrategy(Action<?> action, ActionStrategy... strategies) {
        return action.getStrategy().isEmpty() || action.getStrategy().stream().anyMatch(i -> i.equals(ActionStrategy.STANDARD)) || Arrays.stream(strategies)
            .anyMatch(item -> action.getStrategy().stream().anyMatch(strategy -> strategy.equals(item)));
    }
}
