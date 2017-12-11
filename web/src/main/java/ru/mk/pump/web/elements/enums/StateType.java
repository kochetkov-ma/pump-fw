package ru.mk.pump.web.elements.enums;

import lombok.Getter;

public enum StateType {
    NOT_EXISTS(true),
    NOT_SELENIUM_EXISTS(true),
    NOT_DISPLAYED(true),
    NOT_SELENIUM_DISPLAYED(true),
    NOT_ENABLED(true),
    NOT_SELENIUM_ENABLED(true),
    NOT_SELECTED(true),
    NOT_SELENIUM_SELECTED(true),
    NOT_READY(true),
    NOT_OTHER(true),
    EXISTS(false, NOT_EXISTS),
    SELENIUM_EXISTS(false, NOT_SELENIUM_EXISTS),
    DISPLAYED(false, NOT_DISPLAYED),
    SELENIUM_DISPLAYED(false, NOT_SELENIUM_DISPLAYED),
    ENABLED(false, NOT_ENABLED),
    SELENIUM_ENABLED(false, NOT_SELENIUM_ENABLED),
    SELECTED(false, NOT_SELECTED),
    SELENIUM_SELECTED(false, NOT_SELENIUM_SELECTED),
    READY(false, NOT_READY),
    OTHER(false, NOT_OTHER),
    OR_STATUS_UNION(false);


    @Getter
    private boolean not;

    @Getter
    private boolean or;

    private StateType inverse = null;

    StateType(boolean orUnion) {
        this.not = orUnion;
        this.or = orUnion;
    }

    StateType(boolean orUnion, StateType inverse) {
        this.not = orUnion;
        this.or = orUnion;
        this.inverse = inverse;
    }

    public StateType not() {
        return inverse;
    }

    public StateType or() {
        return inverse;
    }

    @Override
    public String toString() {
        if (isNot()) {
            return "OR_" + super.toString();
        }
        return super.toString();
    }
}
