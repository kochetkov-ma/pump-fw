package ru.mk.pump.web.elements.utils;

import java.util.Map;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.activity.Parameter;

@SuppressWarnings("WeakerAccess")
@UtilityClass
public class Parameters {

    @Nullable
    public <T> T getOrNull(@NotNull Map<String, Parameter<?>> parameterMap, @Nullable String key, @Nullable Class<T> expectedClass) {
        return getOrNull(parameterMap, key, p -> p.getValue(expectedClass));
    }

    @Nullable
    public <T> T getOrNull(Map<String, Parameter<?>> parameterMap, String key, Function<Parameter<?>, T> function) {
        if (parameterMap.containsKey(key)) {
            return function.apply(parameterMap.get(key));
        } else {
            return null;
        }
    }
}