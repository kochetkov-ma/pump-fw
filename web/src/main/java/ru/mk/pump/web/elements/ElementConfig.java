package ru.mk.pump.web.elements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Verifier;

@SuppressWarnings({"unused", "WeakerAccess"})
@Getter
@ToString
public class ElementConfig {

    private Set<Class<? extends Annotation>> requirements = Sets.newHashSet();

    private Map<String, Parameter<?>> parameters = Maps.newHashMap();

    private int index = -1;

    private String name = "";

    private String description = "";

    private Verifier verifier;

    private Reporter reporter;

    public static ElementConfig of(String name) {
        return new ElementConfig().withName(name);
    }

    public static ElementConfig of(String name, String description) {
        return new ElementConfig().withName(name).withDescription(description);
    }

    public ElementConfig withRequirements(@NonNull Collection<Class<? extends Annotation>> requirements) {
        this.requirements.addAll(requirements);
        return this;
    }

    public ElementConfig addRequirement(@NonNull Class<? extends Annotation> requirement) {
        this.requirements.add(requirement);
        return this;
    }

    public ElementConfig withParameters(@NonNull Map<String, Parameter<?>> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    public ElementConfig addParameter(@NonNull String key, Parameter value) {
        this.parameters.put(key, value);
        return this;
    }

    public ElementConfig withIndex(int index) {
        this.index = index;
        return this;
    }

    public ElementConfig withName(@Nullable String name) {
        this.name = name;
        return this;
    }

    public ElementConfig withVerifier(@Nullable Verifier verifier) {
        this.verifier = verifier;
        return this;
    }

    public ElementConfig withReporter(@Nullable Reporter reporter) {
        this.reporter = reporter;
        return this;
    }

    public ElementConfig withDescription(@Nullable String description) {
        this.description = description;
        return this;
    }
}
