package ru.mk.pump.web.elements.internal;


import lombok.Getter;
import ru.mk.pump.commons.utils.WaitResult;

import java.util.concurrent.Callable;
import java.util.function.Consumer;


public class State extends AbstractState<Callable<Boolean>> {

    private State(Callable<Boolean> statePayload, StateType stateType, Consumer<WaitResult<Boolean>> tearDown) {
        super(statePayload, stateType, tearDown);
    }

    @Override
    public Callable<Boolean> get() {
        return getPayload();
    }

    public static State of(Callable<Boolean> callable, StateType stateType, Consumer<WaitResult<Boolean>> tearDown) {
        return new State(callable, stateType, tearDown);
    }

    public static State of(Callable<Boolean> callable, StateType stateType) {
        return new State(callable, stateType, null);
    }

    public enum StateType {
        EXISTS, SELENIUM_EXISTS, DISPLAYED, SELENIUM_DISPLAYED, ENABLED, SELENIUM_ENABLED, SELECTED, SELENIUM_SELECTED, READY, OTHER, OR_STATUS_UNION;

        @Getter
        private boolean not = false;

        public StateType not() {
            this.not = true;
            return this;
        }

        public static StateType not(StateType stateType) {
            return stateType.not();
        }

        @Override
        public String toString() {
            if (isNot()) {
                return "NOT_" + super.toString();
            }
            return super.toString();
        }
    }
}
