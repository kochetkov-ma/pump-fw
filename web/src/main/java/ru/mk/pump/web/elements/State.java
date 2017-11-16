package ru.mk.pump.web.elements;


import java.util.function.BooleanSupplier;

public class State {

    private final BooleanSupplier callable;

    private final StateType stateType;

    private String name = "";

    public State(BooleanSupplier callable, StateType stateType) {
        this.callable = callable;
        this.stateType = stateType;
    }

    boolean check() {
        return callable.getAsBoolean();
    }

    public State withName(String name) {
        this.name = name;
        return this;
    }

    public String name() {
        return name;
    }

    public StateType type() {
        return stateType;
    }

    public enum StateType {
        EXISTS, READY, OTHER
    }
}
