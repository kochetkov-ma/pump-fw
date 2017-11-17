package ru.mk.pump.web.elements;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.elements.Action.ActionStage;
import ru.mk.pump.web.exceptions.ActionExecutingException;

@SuppressWarnings({"WeakerAccess", "unused"})
@NoArgsConstructor
@Slf4j
public class ActionExecutor extends AbstractNotifier {

    private static final int MAX_TRY = 5;

    private final List<Action> beforeActions = new ArrayList<>();

    private final List<Action> afterActions = new ArrayList<>();

    private final List<Action> afterActionError = new ArrayList<>();

    private int actionExecutionTry = 0;

    private StateResolver stateResolver;

    public ActionExecutor(Set<ActionListener> actionListeners) {
        super(actionListeners);
    }

    public ActionExecutor withStateResolver(StateResolver stateResolver) {
        this.stateResolver = stateResolver;
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
            return payloadExecute(tAction);
        } finally {
            if (afterActionError.isEmpty()) {
                tAction.setStage(ActionStage.FINALLY);
                final ActionExecutor helperExecutor = new ActionExecutor(getActionListeners());
                afterActionError.forEach(helperExecutor::payloadExecute);
                notifyOnFinallyAfter(tAction);
            }
            actionExecutionTry = 0;
        }
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

    private <T> T payloadExecute(Action<T> tAction) {
        T result = null;
        ActionExecutor helperExecutor;
        try {
            actionExecutionTry++;
            if (stateResolver != null) {
                tAction.setStage(ActionStage.BEFORE);
                stateResolver.resolve(tAction.getTarget().ready()).result().ifPresent(WaitResult::throwDefaultExceptionOnFail);
            }

            if (beforeActions.isEmpty()) {
                tAction.setStage(ActionStage.BEFORE);
                helperExecutor = new ActionExecutor(getActionListeners());
                beforeActions.forEach(helperExecutor::payloadExecute);
            }

            tAction.setStage(ActionStage.MAIN);
            result = tAction.get();

            if (afterActions.isEmpty()) {
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

}
