package ru.mk.pump.web.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    @NotNull
    public static TestVars of(@NotNull String key, @Nullable Object value) {
        return new TestVars().put(key, value);
    }

    @NotNull
    public static TestVars of(@NotNull Map<String, Object> sourceMap) {
        return new TestVars(sourceMap);
    }

    @NotNull
    public static TestVars of() {
        return new TestVars();
    }

    @NotNull
    public TestVars put(@NotNull String key, @Nullable Object value) {
        sourceMap.put(key, value);
        return this;
    }

    @Nullable
    public Object get(@NotNull String key) {
        return sourceMap.get(key);
    }

    @Nullable
    public Object get(@NotNull String key, @Nullable String defaultValue) {
        return sourceMap.getOrDefault(key, defaultValue);
    }

    public boolean has(@NotNull String key) {
        return sourceMap.containsKey(key);
    }

    @NotNull
    public Map<String, Object> asMap() {
        return ImmutableMap.copyOf(sourceMap);
    }

    @Override
    public String toPrettyString() {
        return Strings.toString(sourceMap);
    }
}