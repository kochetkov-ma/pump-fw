package ru.mk.pump.commons.helpers;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.istack.internal.NotNull;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.mk.pump.commons.constants.StringConstants;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
@ToString
@EqualsAndHashCode
public class Parameters {

    private final Map<String, Parameter<?>> parameterMap;

    private Parameters(@NonNull Set<Parameter<?>> parameterSet) {
        parameterMap = Maps.newHashMap();
        parameterSet.forEach(p -> parameterMap.put(p.getName(), p));
    }

    public static Parameters of(@NonNull Set<Parameter<?>> parameterSet) {
        return new Parameters(parameterSet);
    }

    public static Parameters of() {
        return of(Sets.newHashSet());
    }

    public static Parameters of(@NonNull Parameter<?> parameter) {
        return of().add(parameter);
    }

    public static Parameters of(@NonNull Parameter<?>... parameter) {
        return of().add(parameter);
    }

    @NonNull
    public Parameters add(@NonNull Parameter<?>... parameter) {
        Arrays.stream(parameter).forEach(p -> parameterMap.put(p.getName(), p));
        return this;
    }

    @NonNull
    public Parameters add(@Nullable Parameter<?> parameter) {
        if (parameter != null) {
            this.parameterMap.put(parameter.getName(), parameter);
        }
        return this;
    }

    @NonNull
    public Parameters addAll(@Nullable Parameters parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            this.addAll(parameters.getAll());
        }
        return this;
    }

    @NonNull
    public Parameters addAll(@Nullable Set<Parameter<?>> parameterSet) {
        if (parameterSet != null && !parameterSet.isEmpty()) {
            parameterSet.forEach(p -> parameterMap.put(p.getName(), p));
        }
        return this;
    }

    @NonNull
    public Set<Parameter<?>> getAll() {
        return ImmutableSet.copyOf(parameterMap.values());
    }

    @NotNull
    public Parameter<?> getOrEmpty(@Nullable String name) {
        if (name == null) {
            return Parameter.of(StringConstants.UNDEFINED);
        }
        Parameter<?> res = parameterMap.getOrDefault(name, Parameter.of(StringConstants.UNDEFINED));
        if (res == null) {
            return Parameter.of(StringConstants.UNDEFINED);
        } else {
            return res;
        }
    }

    @NotNull
    public Parameter<?> getOrEmpty(@NonNull Parameter<?> parameter) {
        return getOrEmpty(parameter.getName());
    }

    @NotNull
    public Parameter<?> get(@NonNull Parameter<?> parameter) {
        return get(parameter.getName());
    }

    @Nullable
    public Parameter<?> get(@Nullable String name) {
        if (name == null) {
            return null;
        }
        return parameterMap.get(name);
    }

    public int size() {
        return parameterMap.size();
    }

    @Nullable
    public Parameter<?> getOrDefault(@Nullable String name, @Nullable Parameter<?> defaultValue) {
        if (name == null) {
            return defaultValue;
        }
        return this.parameterMap.getOrDefault(name, defaultValue);
    }

    public boolean has(@Nullable String name) {
        return parameterMap.containsKey(name);
    }

    public boolean has(@Nullable Parameter<?> parameter) {
        return parameter != null && parameterMap.containsKey(parameter.getName());
    }

    public boolean isEmpty() {
        return parameterMap.isEmpty();
    }
}