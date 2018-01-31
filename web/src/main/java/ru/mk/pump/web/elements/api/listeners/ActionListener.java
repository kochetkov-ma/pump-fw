package ru.mk.pump.web.elements.api.listeners;

import ru.mk.pump.commons.listener.Event;
import ru.mk.pump.commons.listener.Listener;
import ru.mk.pump.web.elements.api.listeners.ActionListener.ActionEvent;
import ru.mk.pump.web.elements.internal.interfaces.Action;

public interface ActionListener extends Listener<Action, ActionEvent> {

    @Override
    default void on(Event<Action, ActionEvent> event, Object... args) {
        switch (event.name()) {
            case AFTER_ACTION_FAIL:
                onAfterActionFail(event.get(), Listener.getFromArgsOrNull(Throwable.class, 0, args));
                break;
            case FAIL:
                onFail(event.get(), Listener.getFromArgsOrNull(Throwable.class, 0, args));
                break;
            case FINALLY_STATE_CHECK:
                onFinallyStateCheck(event.get());
                break;
            case SUCCESS:
                onSuccess(event.get(), Listener.getFromArgsOrNull(Object.class, 0, args));
                break;
            case BEFORE_ACTION_SUCCESS:
                onBeforeActionSuccess(event.get());
                break;
        }
    }

    /**
     * Event on : state check, before action and main action is success. But after main action is fail
     */
    void onAfterActionFail(Action action, Throwable throwable);

    /**
     * Event after state checking in finally block AND before main action
     * This event to be call anyway
     */
    void onFinallyStateCheck(Action action);

    /**
     * Event after any error in main action or before action or state check. Exclude error in after action
     */
    void onFail(Action action, Throwable throwable);

    /**
     * Event on : state check and before action and main action is success. Exclude after action
     * @param result main action result
     */
    void onSuccess(Action action, Object result);

    /**
     * Event on : state check and before action is success. Exclude main action and after action
     */
    void onBeforeActionSuccess(Action action);

    enum ActionEvent {
        AFTER_ACTION_FAIL, FAIL, FINALLY_STATE_CHECK, SUCCESS, BEFORE_ACTION_SUCCESS
    }
}
