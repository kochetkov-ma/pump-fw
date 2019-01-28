package ru.mk.pump.web.utils;

import static java.lang.String.format;

import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.listener.AbstractNotifier;
import ru.mk.pump.commons.listener.Event;
import ru.mk.pump.commons.listener.Listener;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Strings;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@ToString
public final class TestVars
        extends AbstractNotifier<Pair<String, Object>, TestVars.TestVarListener.TestVarEvent, TestVars.TestVarListener>
        implements PrettyPrinter {

    @SuppressWarnings("WeakerAccess")
    public final static String RESULT = "result";
    public final static String NULL = "null";
    public final static String NULL_STRING = "null_string";
    public final static String EMPTY_STRING = "empty";
    @SuppressWarnings("WeakerAccess")
    public final static String LAST_PUT = "last";

    private final Map<String, Object> sourceMap;

    private final Map<String, String> sourceStringMap;

    private TestVars(final Map<String, Object> sourceMap, Reporter reporter) {
        /*HashMap because concurrent map not supplies null value*/
        this.sourceMap = new HashMap<>(sourceMap);
        this.sourceStringMap = new HashMap<>();
        this.sourceMap.put(NULL, null);
        this.sourceMap.put(NULL_STRING, "null");
        this.sourceMap.put(EMPTY_STRING, "");
        this.sourceStringMap.putAll(toStringMap());
        addListener(item -> {
            if (reporter != null && !sourceMap.containsKey(item.getKey())) {
                reporter.info(format("Add to global variables with key '%s'", item.getKey()),
                        format("key = %s%n%nvalue = %s", item.getKey(), item.getValue()),
                        reporter.attachments().dummy());
            }
        });
    }

    private TestVars(Map<String, Object> sourceMap) {
        this(sourceMap, null);
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
    public static TestVars of(@NonNull Map<String, Object> sourceMap, @NonNull Reporter reporter) {
        return new TestVars(sourceMap, reporter);
    }

    @NonNull
    public static TestVars of() {
        return new TestVars();
    }

    @NonNull
    public TestVars put(@NonNull String key, @Nullable Object value) {
        sourceMap.put(key, value);
        sourceMap.put(LAST_PUT, value);
        sourceStringMap.put(key, Strings.toString(value));
        notifyOnPut(Pair.of(key, value));
        return this;
    }

    @NonNull
    public TestVars putResult(@Nullable Object value) {
        put(RESULT, value);
        return this;
    }

    @Nullable
    public Object getResult() {
        return sourceMap.get(RESULT);
    }

    @Nullable
    public Object getLastPut() {
        return sourceMap.get(LAST_PUT);
    }

    @Nullable
    public Object get(@NonNull String key) {
        return sourceMap.get(key);
    }

    @Nullable
    public Object get(@NonNull String key, @Nullable String defaultValue) {
        return sourceMap.getOrDefault(key, defaultValue);
    }

    public Map<String, String> getStringMap() {
        return Collections.unmodifiableMap(sourceStringMap);
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

    protected void notifyOnPut(Pair<String, Object> object) {
        notify(event(object, TestVars.TestVarListener.TestVarEvent.PUT_VAR));
    }

    private Map<String, String> toStringMap() {
        return sourceMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, i -> Strings.toString(i.getValue())));
    }

    public interface TestVarListener extends Listener<Pair<String, Object>, TestVarListener.TestVarEvent> {

        @Override
        default void on(Event<Pair<String, Object>, TestVarListener.TestVarEvent> event, Object... args) {
            switch (event.name()) {
                case PUT_VAR:
                    onPut(event.get());
                    break;
            }
        }

        void onPut(Pair<String, Object> item);

        enum TestVarEvent {
            PUT_VAR
        }
    }
}