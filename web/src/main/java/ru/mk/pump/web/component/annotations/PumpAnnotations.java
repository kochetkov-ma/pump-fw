package ru.mk.pump.web.component.annotations;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.pagefactory.Annotations;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.component.internal.ParameterTransformer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class PumpAnnotations extends Annotations {

    public PumpAnnotations(Field field) {
        super(field);
    }

    public Map<String, Parameter<?>> buildParameters() {
        final Map<String, Parameter<?>> parameters = Maps.newHashMap();

        for (Annotation annotation : getField().getDeclaredAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(ParameterTransformerAnnotation.class)) {
                try {
                    final ParameterTransformer parameterBuilder = annotation.annotationType()
                            .getAnnotation(ParameterTransformerAnnotation.class).value()
                            .newInstance();
                    if (parameterBuilder != null) {
                        //noinspection unchecked
                        parameters.entrySet().add(parameterBuilder.trasform(annotation));
                    } else {
                        log.error("Cannot find parameter transformer for annotation '{}'", annotation);
                    }
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException(String.format("Error when try to create transformer '%s' for annotation '%s'", annotation.annotationType()
                            .getAnnotation(ParameterTransformerAnnotation.class).value().getClass().getSimpleName(), annotation));
                }
            }
        }
        return parameters;
    }
}
