package ru.mk.pump.web.elements;

import static java.lang.String.format;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.elements.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.exceptions.ElementDiscoveryException;

@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
@ToString(of = {"DEFAULT_ANNOTATION_IMPL", "DEFAULT_PACKAGE_IMPL", "DEFAULT_PACKAGE_INTERFACE", "interfaceToImplMap"})
@Slf4j
public class ElementImplDispatcher implements StrictInfo {

    private final static Class<? extends Annotation> DEFAULT_ANNOTATION_IMPL = FrameworkImpl.class;

    private final static String[] DEFAULT_PACKAGE_IMPL = new String[]{"ru.mk.pump.web.elements.internal.impl"};

    private final static String[] DEFAULT_PACKAGE_INTERFACE = new String[]{"ru.mk.pump.web.elements.api.concrete"};

    private final SetMultimap<Class<? extends Element>, ElementImpl<? extends BaseElement>> interfaceToImplMap;

    public ElementImplDispatcher() {
        this(HashMultimap.create());
    }

    public ElementImplDispatcher(SetMultimap<Class<? extends Element>, ElementImpl<? extends BaseElement>> interfaceToImplMap) {
        this.interfaceToImplMap = interfaceToImplMap;
        loadDefault();
    }

    public <R extends BaseElement> ElementImpl<R> findImplementation(@NotNull Class<? extends Element> elementInterface,
        @Nullable Set<Class<? extends Annotation>> requirements) {
        return findByElementConfig(elementInterface, requirements);
    }

    public <T extends Element, V extends BaseElement> ElementImplDispatcher addImplementation(@NotNull Class<T> elementInterface,
        @NotNull ElementImpl<V> elementImplementation) {
        interfaceToImplMap.put(elementInterface, elementImplementation);
        return null;
    }

    public Multimap<Class<? extends Element>, ElementImpl<? extends BaseElement>> getAll() {
        return interfaceToImplMap;
    }

    public <T extends BaseElement> long addParams(@NotNull Class<T> elementImpl, @NotNull Map<String, Parameter<?>> parameters) {
        return getAll().values().stream()
            .filter(impl -> elementImpl.isAssignableFrom(impl.getImplementation()))
            .map(impl -> impl.setParameters(parameters))
            .count();
    }

    @Override
    public Map<String, String> getInfo() {
        final LinkedHashMap<String, String> res = Maps.newLinkedHashMap();
        res.put("DEFAULT_PACKAGE_INTERFACE", Arrays.toString(DEFAULT_PACKAGE_INTERFACE));
        res.put("DEFAULT_PACKAGE_IMPL", Arrays.toString(DEFAULT_PACKAGE_IMPL));
        res.put("DEFAULT_ANNOTATION_IMPL", DEFAULT_ANNOTATION_IMPL.getSimpleName());
        interfaceToImplMap.asMap()
            .forEach((key, value) -> res.put(key.getSimpleName(), Strings.toPrettyString(value)));
        return res;
    }

    protected void loadDefault() {
        final Reflections reflectionsImpl = new Reflections((Object[]) DEFAULT_PACKAGE_IMPL);
        final Reflections reflectionsInterface = new Reflections((Object[]) DEFAULT_PACKAGE_INTERFACE);
        for (Class<? extends Element> aClass : reflectionsInterface.getSubTypesOf(Element.class)) {
            interfaceToImplMap.putAll(aClass, reflectionsImpl.getSubTypesOf(BaseElement.class).stream()
                .filter(aClass::isAssignableFrom)
                .map(item -> ElementImpl.of(item, null))
                .collect(Collectors.toList())
            );
        }
        addImplementation(Element.class, ElementImpl.of(BaseElement.class, null));
    }

    @SuppressWarnings("WeakerAccess")
    @Getter
    @EqualsAndHashCode(of = {"implementation"})
    public static class ElementImpl<T extends BaseElement> {

        private final Class<T> implementation;

        private Map<String, Parameter<?>> parameters;

        private ElementImpl(@NotNull Class<T> implementation, @Nullable Map<String, Parameter<?>> parameters) {
            this.implementation = implementation;
            this.parameters = parameters;
        }

        public static <T extends BaseElement> ElementImpl<T> of(@NotNull Class<T> implementation, @Nullable Map<String, Parameter<?>> parameters) {
            return new ElementImpl<>(implementation, parameters);
        }

        public ElementImpl<T> setParameters(Map<String, Parameter<?>> parameters) {
            this.parameters = parameters;
            return this;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ElementImpl(");
            sb.append("implementation=").append(implementation.getName());
            if (parameters != null && !parameters.isEmpty()) {
                sb.append(", parameters=").append(parameters);
            }
            sb.append(')');
            return sb.toString();
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Element, R extends BaseElement> ElementImpl<R> findByElementConfig(Class<T> elementInterface,
        @Nullable Set<Class<? extends Annotation>> requirements) {
        if (!interfaceToImplMap.containsKey(elementInterface)) {
            throw new ElementDiscoveryException(
                new PumpMessage(format("Interface '%s' do not exists in ElementImplDispatcher. You have to add this using #addImplementation",
                    elementInterface.getSimpleName()))
                    .addEnvInfo(this));
        }
        final Collection<ElementImpl<? extends BaseElement>> implementations = interfaceToImplMap.get(elementInterface);
        if (implementations.isEmpty()) {
            throw new ElementDiscoveryException(
                new PumpMessage(
                    format("Interface '%s' do not have any implementations  in ElementImplDispatcher. You have to add using #addImplementation",
                        elementInterface.getSimpleName()))
                    .addEnvInfo(this));
        }
        Optional<ElementImpl<? extends BaseElement>> expectedImpl = Optional.empty();
        if (requirements != null && !requirements.isEmpty()) {
            expectedImpl = implementations.stream()
                .filter(item -> requirements.stream().allMatch(an -> item.getImplementation().isAnnotationPresent(an)))
                .findFirst();
            if (!expectedImpl.isPresent()) {
                log.warn("[ElementImplDispatcher] Cannot find implementation with all of this annotations '{}'", requirements);
            }
        }
        if (!expectedImpl.isPresent()) {
            expectedImpl = implementations.stream()
                .filter(item -> item.getImplementation().isAnnotationPresent(FrameworkImpl.class)).findFirst();
        }
        //TODO::may be ClassCastException
        if (expectedImpl.isPresent()) {
            return (ElementImpl<R>) expectedImpl.orElseThrow(UnknownError::new);
        }
        log.debug("[ElementImplDispatcher] Cannot find implementation with default annotation '{}'", DEFAULT_ANNOTATION_IMPL);
        return (ElementImpl<R>) implementations.iterator().next();
    }
}