package ru.mk.pump.web.elements.api;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Verifier;

@SuppressWarnings({"unused", "WeakerAccess"})
@Getter
public class ElementConfig {

    private Set<Class<? extends Annotation>> requirements = Sets.newHashSet();

    private Set<Annotation> annotations = Sets.newHashSet();

    private Map<String, Parameter<?>> parameters = Maps.newHashMap();

    private int index = -1;

    private String name = "";

    private String description = "";

    private Verifier verifier;

    private Reporter reporter;

    public static ElementConfig of(String name, Annotation... annotations) {
        return new ElementConfig().withName(name).withAnnotations(Arrays.asList(annotations));
    }

    public static ElementConfig of(String name) {
        return new ElementConfig().withName(name);
    }

    public static ElementConfig of(String name, String description) {
        return new ElementConfig().withName(name).withDescription(description);
    }

    public ElementConfig withRequirements(@NotNull Collection<Class<? extends Annotation>> requirements) {
        this.requirements.addAll(requirements);
        return this;
    }

    public ElementConfig addRequirement(@NotNull Class<? extends Annotation> requirement) {
        this.requirements.add(requirement);
        return this;
    }

    public ElementConfig withAnnotations(@NotNull Collection<Annotation> annotations) {
        this.annotations.addAll(annotations);
        return this;
    }

    public ElementConfig addAnnotation(@NotNull Annotation annotation) {
        this.annotations.add(annotation);
        return this;
    }

    public ElementConfig withParameters(@NotNull Map<String, Parameter<?>> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    public ElementConfig addParameter(@NotNull String key, Parameter value) {
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
