package ru.mk.pump.web.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.ToString;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.utils.Strings;

@SuppressWarnings("unused")
@ToString
public final class TestVars implements PrettyPrinter {

    private final Map<String, Object> sourceMap;

    private TestVars(Map<String, Object> sourceMap) {
        /*HashMap because concurrent map not supplies null value*/
        this.sourceMap = new HashMap<>(sourceMap);
    }

    private TestVars() {
        this(Maps.newHashMap());
    }

    @NonNull
    public static TestVars of(@NonNull String key, @Nullable Object value) {
        return new TestVars().put(key, value);
    }

    @NonNull
    public static TestVars of(@NonNull Map<String, Object> sourceMap) {
        return new TestVars(sourceMap);
    }

    @NonNull
    public static TestVars of() {
        return new TestVars();
    }

    @NonNull
    public TestVars put(@NonNull String key, @Nullable Object value) {
        sourceMap.put(key, value);
        return this;
    }

    @Nullable
    public Object get(@NonNull String key) {
        return sourceMap.get(key);
    }

    @Nullable
    public Object get(@NonNull String key, @Nullable String defaultValue) {
        return sourceMap.getOrDefault(key, defaultValue);
    }

    public boolean has(@NonNull String key) {
        return sourceMap.containsKey(key);
    }

    @NonNull
    public Map<String, Object> asMap() {
        return ImmutableMap.copyOf(sourceMap);
    }

    @Override
    public String toPrettyString() {
        return Strings.toString(sourceMap);
    }
}