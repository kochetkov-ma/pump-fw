package ru.mk.pump.commons.utils;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unused", "WeakerAccess"})
@ThreadSafe
@UtilityClass
public class EnvVariables {

    @Getter
    private static final Map<String, String> usedVariablesMap = Maps.newHashMap();

    private Map<String, String> cache;

    /**
     * If system variables may be changed during app executing
     */
    synchronized public void reloadCache() {
        cache = null;
    }

    @NonNull
    public Map<String, String> all() {
        /*result for thread safe*/
        Map<String, String> result = cache;
        if (result == null) {
            result = Maps.newHashMap(System.getenv());
            result.putAll(PropertiesUtil.propertiesToMap(System.getProperties()));
            cache = result;
        }
        return result;
    }

    public boolean has(@NonNull String name) {
        return all().containsKey(name);
    }

    @NonNull
    public String get(@NonNull String name, @NonNull String defaultValue) {
        final String value = all().getOrDefault(name, defaultValue);
        usedVariablesMap.put(name, value);
        return value;
    }

    @Nullable
    public String get(@NonNull String name) {
        return all().get(name);
    }
}
