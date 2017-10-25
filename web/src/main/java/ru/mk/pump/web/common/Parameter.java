package ru.mk.pump.web.common;


import java.util.function.Function;

public final class Parameter<T> {

    private final Function<String, T> stringTFunction;

    private final Class<T> tClass;

    private T value;

    private String stringValue;

    private Parameter(Function<String, T> stringTFunction, Class<T> tClass) {
        this.stringTFunction = stringTFunction;
        this.tClass = tClass;
    }

    public static Parameter<String> of(String value) {
        return new Parameter<>(null, String.class).withValue(value);
    }

    public static <T> Parameter<T> of(Function<String, T> stringTFunction, Class<T> tClass) {
        return new Parameter<>(stringTFunction, tClass);
    }

    public Parameter<T> withValue(String stringValue) {
        this.stringValue = stringValue;
        this.value = stringTFunction.apply(stringValue);
        return this;
    }

    public String asString() {
        return stringValue;
    }

    public <V> V getValue(Class<V> candidateClass) {
        if (isClass(candidateClass)) {
            return (V) getValue();
        } else {
            throw new IllegalArgumentException(String.format("Parameter '%s' cannot cast to class '%s'", asString(), candidateClass.getSimpleName()));
        }
    }

    public T getValue() {
        return value;
    }

    public boolean isClass(Class pClass) {
        return pClass.isAssignableFrom(tClass);
    }

    public Class<T> getParameterClass() {
        return tClass;
    }
}
