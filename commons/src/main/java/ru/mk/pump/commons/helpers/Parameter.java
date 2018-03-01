package ru.mk.pump.commons.helpers;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.utils.Strings;

import javax.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString
@Getter
@EqualsAndHashCode(of = {"name", "tClass", "stringValue", "value"}, doNotUseGetters=true)
public final class Parameter<T> {

    @NonNull
    private final String name;

    private final Class<T> tClass;

    private final T value;

    private final String stringValue;

    private Parameter(@NonNull String name, @NonNull Class<T> tClass, @Nullable T value, @Nullable String stringValue) {
        this.name = name;
        this.tClass = tClass;
        this.value = value;
        if (stringValue != null) {
            this.stringValue = stringValue;
        } else {
            if (value != null) {
                this.stringValue = Strings.toString(value);
            } else {
                this.stringValue = Strings.empty();
            }
        }
    }

    @NonNull
    public static <T> Parameter<T> of(@NonNull String name, @NonNull Class<T> tClass, @Nullable T value, @Nullable String stringValue) {
        if (tClass == String.class) {
            if (!Strings.isEmpty(stringValue)) {
                //noinspection unchecked
                return (Parameter<T>) new Parameter<>(name, String.class, stringValue, stringValue);
            } else if (value != null && !((String) value).isEmpty()) {
                //noinspection unchecked
                return (Parameter<T>) new Parameter<>(name, String.class, (String) value, (String) value);
            }
        }
        return new Parameter<>(name, tClass, value, stringValue);
    }

    @NonNull
    public static <T> Parameter<T> of(@NonNull String name, @NonNull Class<T> tClass, @Nullable T value) {
        return of(name, tClass, value, null);
    }

    @NonNull
    public static <T> Parameter<T> of(@NonNull String name, @NonNull Class<T> tClass, @Nullable String stringValue) {
        return of(name, tClass, null, stringValue);
    }

    @NonNull
    public static <T> Parameter<T> of(@NonNull String name, @NonNull Class<T> tClass) {
        return of(name, tClass, null, null);
    }

    @NonNull
    public static Parameter<String> of(@NonNull String name, @Nullable String stringValue) {
        return of(name, String.class, stringValue, stringValue);
    }

    @NonNull
    public static Parameter<String> of(@Nullable String stringValue) {
        return of(StringConstants.UNDEFINED, String.class, stringValue, stringValue);
    }

    @NonNull
    public static <T> Parameter<T> of(@NonNull Class<T> tClass, @Nullable T value) {
        return of(StringConstants.UNDEFINED, tClass, value, null);
    }

    @NonNull
    public Parameter<T> withName(@NonNull String name) {
        return of(name, tClass, value, stringValue);
    }

    @NonNull
    public Parameter<T> withValue(@Nullable T value) {
        return of(name, tClass, value, stringValue);
    }

    @NonNull
    public Parameter<T> withStringValue(@Nullable String stringValue) {
        return of(name, tClass, value, stringValue);
    }

    @NonNull
    public String getStringValue() {
        return stringValue;
    }

    @Nullable
    public <V> V getValue(Class<V> expectedClass) {
        if (value == null) {
            return null;
        }
        checkClass(expectedClass);
        //noinspection unchecked
        return (V) value;
    }

    public void checkClass(@NonNull Class<?> pClass) {
        if (!isClass(pClass)) {
            throw new IllegalArgumentException(String.format("Parameter '%s' cannot cast to class '%s'", toString(), pClass.getSimpleName()));
        }
    }

    public boolean isClass(@NonNull Class<?> pClass) {
        return pClass != null && pClass.isAssignableFrom(tClass);
    }

    public Class<T> getParameterClass() {
        return tClass;
    }
}