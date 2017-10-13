package ru.mk.pump.commons.utils;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.exception.UtilException;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@UtilityClass
public class EnvVariables {

    @Getter
    private static final Map<String, String> usedVariablesMap = Maps.newHashMap();

    public boolean has(String name) {
        return System.getenv(name) != null;
    }

    @NotNull
    public String get(String name, String defaultValue) {
        String value = System.getenv(name);
        if (value == null) {
            if (defaultValue != null) {
                value = defaultValue;
            } else {
                throw new UtilException(String.format("Cannot find variables by name '%s' and defaultValue is null", name));
            }
        }
        usedVariablesMap.put(name, value);
        return value;
    }

    @NotNull
    public String get(String name) {
        return get(name, null);
    }
}
