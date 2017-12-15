package ru.mk.pump.web.elements.enums;

public enum ActionStrategy {
    /**
     * Full execution
     */
    STANDARD,
    /**
     * Without after actions
     */
    NO_AFTER,
    /**
     * Without before actions such as scroll
     */
    NO_BEFORE,
    /**
     * Without finally block at the end
     */
    NO_FINALLY,
    /**
     * NO_AFTER + NO_BEFORE + NO_FINALLY but with CHECK_STATE
     */
    SIMPLE,
    /**
     * Without check state before action
     */
    NO_STATE_CHECK

}
