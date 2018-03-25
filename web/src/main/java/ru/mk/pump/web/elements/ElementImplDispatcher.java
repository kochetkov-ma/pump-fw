package ru.mk.pump.web.elements;

import static java.lang.String.format;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.commons.utils.ReflectionUtils;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.common.api.PageItemImplDispatcher;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.annotations.CustomImpl;
import ru.mk.pump.web.elements.api.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.annotations.Requirements;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.exceptions.ElementDiscoveryException;

@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
@ToString(of = {"DEFAULT_ANNOTATION_IMPL", "DEFAULT_PACKAGE_IMPL", "customPackagesImpl", "DEFAULT_PACKAGE_INTERFACE", "customPackagesInterface",
    "interfaceToImplMap"})
@Slf4j
public class ElementImplDispatcher implements PageItemImplDispatcher {

    private final static Class<? extends Annotation> DEFAULT_ANNOTATION_IMPL = FrameworkImpl.class;

    private final static Class<? extends Annotation> DEFAULT_ANNOTATION_CUSTOM = CustomImpl.class;

    private final static String[] DEFAULT_PACKAGE_IMPL = new String[]{"ru.mk.pump.web.elements.internal.impl"};

    private final static String[] DEFAULT_PACKAGE_INTERFACE = new String[]{"ru.mk.pump.web.elements.api.concrete"};

    @Setter
    public static String[] customPackagesImpl = ConfigurationHolder.get().getElement().getPackagesImpl();

    @Setter
    public static String[] customPackagesInterface = ConfigurationHolder.get().getElement().getPackagesInterface();

    private final SetMultimap<Class<? extends Element>, ElementImpl<? extends BaseElement>> interfaceToImplMap;

    @SuppressWarnings("WeakerAccess")
    @Getter
    @EqualsAndHashCode(of = {"implementation"})
    public static class ElementImpl<T extends BaseElement> {

        private final Class<T> implementation;

        private Parameters parameters;

        private ElementImpl(@NonNull Class<T> implementation, @Nullable Parameters parameters) {
            this.implementation = implementation;
            this.parameters = parameters;
        }

        public static <T extends BaseElement> ElementImpl<T> of(@NonNull Class<T> implementation, @Nullable Parameters parameters) {
            return new ElementImpl<>(implementation, parameters);
        }

        public ElementImpl<T> setParameters(Parameters parameters) {
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

    public ElementImplDispatcher() {
        this(HashMultimap.create());
    }

    public ElementImplDispatcher(SetMultimap<Class<? extends Element>, ElementImpl<? extends BaseElement>> interfaceToImplMap) {
        this.interfaceToImplMap = interfaceToImplMap;
        loadDefault();
    }

    @Override
    public <R extends BaseElement> ElementImpl<R> findImplementation(@NonNull Class<? extends Element> elementInterface,
        @Nullable Set<Class<? extends Annotation>> requirements) {
        final ElementImpl<R> result = findByElementConfig(elementInterface, requirements);
        log.debug("[ElementImplDispatcher] find implementation for interface '{}' is '{}'", elementInterface, result);
        return result;
    }

    public <T extends Element, V extends BaseElement> ElementImplDispatcher addImplementation(@NonNull Class<T> elementInterface,
        @NonNull ElementImpl<V> elementImplementation) {
        interfaceToImplMap.put(elementInterface, elementImplementation);
        return null;
    }

    public Multimap<Class<? extends Element>, ElementImpl<? extends BaseElement>> getAll() {
        return interfaceToImplMap;
    }

    public <T extends BaseElement> long addParams(@NonNull Class<T> elementImpl, @NonNull Parameters parameters) {
        return getAll().values().stream()
            .filter(impl -> elementImpl.isAssignableFrom(impl.getImplementation()))
            .map(impl -> impl.setParameters(parameters))
            .count();
    }

    @Override
    public Map<String, String> getInfo() {
        final LinkedHashMap<String, String> res = Maps.newLinkedHashMap();
        res.put("PACKAGE_INTERFACE", Arrays.toString(ArrayUtils.addAll(DEFAULT_PACKAGE_INTERFACE, customPackagesInterface)));
        res.put("PACKAGE_IMPL", Arrays.toString(ArrayUtils.addAll(DEFAULT_PACKAGE_IMPL, customPackagesImpl)));
        res.put("DEFAULT_ANNOTATION_IMPL", DEFAULT_ANNOTATION_IMPL.getSimpleName());
        interfaceToImplMap.asMap()
            .forEach((key, value) -> res.put(key.getSimpleName(), Strings.toPrettyString(value)));
        return res;
    }

    protected void loadDefault() {
        Set<Class<? extends BaseElement>> implementations = ReflectionUtils
            .getAllClasses(BaseElement.class, ArrayUtils.addAll(DEFAULT_PACKAGE_IMPL, customPackagesImpl));
        Set<Class<? extends Element>> interfaces = ReflectionUtils
            .getAllClasses(Element.class, ArrayUtils.addAll(DEFAULT_PACKAGE_INTERFACE, customPackagesInterface));
        for (Class<? extends Element> aClass : interfaces.stream().filter(Class::isInterface).collect(Collectors.toList())) {
            interfaceToImplMap.putAll(aClass, implementations.stream()
                .filter(cls -> ReflectionUtils.hasInterfaceOrSuperclass(aClass, cls))
                .map(item -> ElementImpl.of(item, null))
                .collect(Collectors.toList())
            );
        }
        addImplementation(Element.class, ElementImpl.of(BaseElement.class, null));
        log.debug("[ElementImplDispatcher] implementation load - success : {}", Strings.toPrettyString(getInfo()));
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
                .filter(item -> requirements.stream()
                    .filter(a -> a.isAnnotationPresent(Requirements.class))
                    .allMatch(an -> item.getImplementation().isAnnotationPresent(an)))
                .min(Comparator.comparingInt(e -> e.getImplementation().getAnnotations().length));
            if (!expectedImpl.isPresent()) {
                log.warn("[ElementImplDispatcher] Cannot find implementation with all of this annotations '{}'", requirements);
            }
        }
        if (!expectedImpl.isPresent()) {
            expectedImpl = implementations.stream()
                .filter(item -> item.getImplementation().isAnnotationPresent(DEFAULT_ANNOTATION_CUSTOM))
                .min(Comparator.comparingInt(e -> e.getImplementation().getAnnotations().length));
        }
        if (!expectedImpl.isPresent()) {
            expectedImpl = implementations.stream()
                .filter(item -> item.getImplementation().isAnnotationPresent(DEFAULT_ANNOTATION_IMPL))
                .min(Comparator.comparingInt(e -> e.getImplementation().getAnnotations().length));
        }
        //TODO::may be ClassCastException
        if (expectedImpl.isPresent()) {
            return (ElementImpl<R>) expectedImpl.orElseThrow(UnknownError::new);
        }
        log.debug("[ElementImplDispatcher] Cannot find implementation of '{}' with default annotation '{}'", elementInterface, DEFAULT_ANNOTATION_IMPL);
        return (ElementImpl<R>) implementations.iterator().next();
    }
}