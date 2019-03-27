package ru.mk.pump.commons.helpers;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.utils.Str;

import javax.annotation.Nullable;

/**
 * It is non modify class!
 * All methods start on 'of' or 'with' generate NEW instance
 *
 * @param <T> Object.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@ToString
@Getter
@EqualsAndHashCode(of = {"name", "valueClass", "stringValue", "value"}, doNotUseGetters = true)
public final class Parameter<T> {

    @NonNull
    private final String name;

    private final Class<T> valueClass;

    private final T value;

    private final String stringValue;

    private Parameter(@NonNull String name, @NonNull Class<T> valueClass, @Nullable T value, @Nullable String stringValue) {
        this.name = name;
        this.valueClass = valueClass;
        this.value = value;
        if (stringValue != null) {
            this.stringValue = stringValue;
        } else {
            if (value != null) {
                this.stringValue = Str.toString(value);
            } else {
                this.stringValue = Str.empty();
            }
        }
    }

    @NonNull
    public static <T> Parameter<T> of(@NonNull String name, @NonNull Class<T> tClass, @Nullable T value, @Nullable String stringValue) {
        if (tClass == String.class) {
            if (!Str.isEmpty(stringValue)) {
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

    /**
     * @param name Name
     *
     * @return New parameter with name
     */
    @NonNull
    public Parameter<T> withName(@NonNull String name) {
        return of(name, valueClass, value, stringValue);
    }

    /**
     * Use if generic type was not erased
     *
     * @return New parameter with value.
     */
    @NonNull
    public Parameter<T> withStrictValue(@Nullable T value) {
        return of(name, valueClass, value, stringValue);
    }

    /**
     * @return New parameter with value
     */
    @NonNull
    public Parameter<T> withValue(@Nullable Object value) {
        if (value != null) {
            checkClass(value.getClass());
        }
        //noinspection unchecked
        return of(name, valueClass, (T) value, stringValue);
    }

    /**
     * @return New parameter with stringValue
     */
    @NonNull
    public Parameter<T> withStringValue(@Nullable String stringValue) {
        return of(name, valueClass, value, stringValue);
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

    /**
     * @throws IllegalArgumentException - If 'valueClass' is not 'pClass'
     */
    public void checkClass(@NonNull Class<?> pClass) {
        if (!isClass(pClass)) {
            throw new IllegalArgumentException(String.format("Parameter '%s' cannot cast to class '%s'", toString(), pClass.getSimpleName()));
        }
    }

    /**
     * @return true - If 'pClass.isAssignableFrom(valueClass)'
     */
    public boolean isClass(@NonNull Class<?> pClass) {
        return pClass != null && pClass.isAssignableFrom(valueClass);
    }
}