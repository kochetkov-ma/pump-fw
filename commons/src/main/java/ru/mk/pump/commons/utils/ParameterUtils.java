package ru.mk.pump.commons.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;

import javax.annotation.Nullable;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
@UtilityClass
public class ParameterUtils {

    @Nullable
    public <T> T getOrDefault(@NonNull Parameters parameters, @NonNull String key, @Nullable Class<T> expectedClass,
                              @Nullable T defaultValue) {
        return getOrDefault(parameters, key, p -> getAdvanced(p, expectedClass), defaultValue);
    }

    @Nullable
    public <T> T getOrDefault(@NonNull Parameters parameters, @NonNull String key, @NonNull Function<Parameter<?>, T> function,
                              @Nullable T defaultValue) {
        final T res = getOrNull(parameters, key, function);
        return res != null ? res : defaultValue;
    }

    @Nullable
    public <T> T getOrNull(@NonNull Parameters parameters, @NonNull String key, @Nullable Class<T> expectedClass) {
        return getOrNull(parameters, key, p -> getAdvanced(p, expectedClass));
    }

    @Nullable
    public <T> T getOrNull(@NonNull Parameters parameters, @NonNull String key, @NonNull Function<Parameter<?>, T> function) {
        if (parameters.has(key)) {
            return function.apply(parameters.get(key));
        } else {
            return null;
        }
    }

    @Nullable
    public <T> T getAdvanced(@NonNull Parameter<?> parameter, @NonNull Class<T> expectedClass) {
        parameter.checkClass(expectedClass);
        if (parameter.getValue() != null) {
            //noinspection unchecked
            return (T) parameter.getValue();
        }
        if (Str.isEmpty(parameter.getStringValue())){
            return null;
        }
        try {
            return Str.toObject(parameter.getStringValue(), expectedClass);
        } catch (Exception ignore){
            return null;
        }
    }
}