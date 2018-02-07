package ru.mk.pump.web.common.pageobject;

import com.google.common.collect.Maps;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.support.pagefactory.Annotations;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.common.api.ParameterTransformer;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.elements.api.annotations.Requirements;

@Slf4j
class PumpElementAnnotations extends Annotations {

    PumpElementAnnotations(Field field) {
        super(field);
    }

    @NotNull Map<String, Parameter<?>> buildParameters() {
        final Map<String, Parameter<?>> parameters = Maps.newHashMap();

        for (Annotation annotation : getField().getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(ParameterTransformerAnnotation.class)) {
                try {
                    final ParameterTransformer parameterBuilder = annotation.annotationType()
                        .getAnnotation(ParameterTransformerAnnotation.class).value()
                        .newInstance();
                    if (parameterBuilder != null) {
                        //noinspection unchecked
                        parameters.putAll(parameterBuilder.transform(annotation));
                    } else {
                        log.error("Cannot find parameter transformer for annotation '{}' and field '{}'", annotation, getField());
                    }
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException(
                        String.format("Error when try to create transformer '%s' for annotation '%s' and field '%s'", annotation.annotationType()
                            .getAnnotation(ParameterTransformerAnnotation.class).value().getClass().getSimpleName(), annotation, getField()));
                }
            }
        }
        return parameters;
    }

    @NotNull String getName() {
        if (getField().isAnnotationPresent(PElement.class)){
            return getField().getAnnotation(PElement.class).value();
        }
        return "";
    }

    @NotNull String getDescription() {
        if (getField().isAnnotationPresent(PElement.class)){
            return getField().getAnnotation(PElement.class).desc();
        }
        return "";
    }

    @NotNull Collection<Class<? extends Annotation>> getRequirements() {
        return Arrays.stream(getField().getAnnotations())
            .map(Annotation::getClass)
            .filter(a -> a.isAnnotationPresent(Requirements.class))
            .collect(Collectors.toList());

    }
}