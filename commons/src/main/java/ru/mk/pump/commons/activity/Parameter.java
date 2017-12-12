package ru.mk.pump.commons.activity;


import java.util.function.Function;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class Parameter<T> {

    private final Function<String, T> stringTFunction;

    private final Class<T> tClass;

    private T value;

    private String stringValue = "undefined";

    private Parameter(Function<String, T> stringTFunction, Class<T> tClass) {
        this.stringTFunction = stringTFunction;
        this.tClass = tClass;
    }

    private Parameter(Class<T> tClass, T value, String stringValue) {
        this.value = value;
        this.tClass = tClass;
        this.stringValue = stringValue;
        this.stringTFunction = null;
    }

    private Parameter(Class<T> tClass, T value) {
        this.value = value;
        this.tClass = tClass;
        this.stringTFunction = null;
    }

    public static Parameter<String> of(String value) {
        return new Parameter<>((str) -> str, String.class).withValue(value);
    }

    public static <T> Parameter<T> of(Class<T> tClass, T value) {
        return new Parameter<>(tClass, value);
    }

    public static <T> Parameter<T> of(Class<T> tClass, T value, String stringPresentValue) {
        return new Parameter<>(tClass, value, stringPresentValue);
    }

    public static <T> Parameter<T> of(Function<String, T> stringTFunction, Class<T> tClass) {
        return new Parameter<>(stringTFunction, tClass);
    }

    public Parameter<T> withValue(String stringValue) {
        this.stringValue = stringValue;
        if (stringTFunction != null) {
            this.value = stringTFunction.apply(stringValue);
        }
        return this;
    }

    public String asString() {
        return stringValue;
    }


    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    public boolean isClass(Class pClass) {
        if (pClass == null) {
            return false;
        }
        return pClass.isAssignableFrom(tClass);
    }

    public Class<T> getParameterClass() {
        return tClass;
    }

    @Override
    public String toString() {
        return "Parameter(tClass=" + tClass.getSimpleName()
            + ", value=" + value
            + ", stringValue='" + stringValue + '\''
            + ')';
    }
}
