package ru.mk.pump.web.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.ToString;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.utils.Strings;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@ToString
public final class TestVars implements PrettyPrinter {

    @SuppressWarnings("WeakerAccess")
    public final static String RESULT = "result";
    public final static String NULL = "null";
    public final static String NULL_STRING = "null_string";
    public final static String EMPTY_STRING = "empty";
    @SuppressWarnings("WeakerAccess")
    public final static String LAST_PUT = "last";

    private final Map<String, Object> sourceMap;

    private TestVars(Map<String, Object> sourceMap) {
        /*HashMap because concurrent map not supplies null value*/
        this.sourceMap = new HashMap<>(sourceMap);
        this.sourceMap.put(NULL, null);
        this.sourceMap.put(NULL_STRING, "null");
        this.sourceMap.put(EMPTY_STRING, "");
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
        sourceMap.put(LAST_PUT, value);
        return this;
    }

    @NonNull
    public TestVars putResult(@Nullable Object value) {
        sourceMap.put(RESULT, value);
        return this;
    }

    @Nullable
    public Object getResult() {
        return sourceMap.get(RESULT);
    }

    @Nullable
    public Object getLastPut() {
        return  sourceMap.get(LAST_PUT);
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
        return Collections.unmodifiableMap(sourceMap);
    }

    @Override
    public String toPrettyString() {
        return Strings.toPrettyString(sourceMap);
    }
}