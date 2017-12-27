package ru.mk.pump.web.component.annotations;

import com.google.common.collect.Lists;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.support.pagefactory.Annotations;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.component.internal.AbstractPumpParameterBuilder;

public class PumpAnnotations extends Annotations {

    public PumpAnnotations(Field field) {
        super(field);
    }

    public List<Parameter<?>> buildParameters() {
        // assertValidPumpAnnotations();

        final List<Parameter<?>> parameters = Lists.newArrayList();

        for (Annotation annotation : getField().getDeclaredAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(ComponentParameterBuilder.class)) {
                try {
                    final AbstractPumpParameterBuilder parameterBuilder = annotation.annotationType()
                        .getAnnotation(ComponentParameterBuilder.class).value()
                        .newInstance();
                    if (parameterBuilder != null) {
                        parameters.add(parameterBuilder.buildIt());
                    } else {
                        // Fall through.
                    }
                } catch (ReflectiveOperationException e) {
                    // Fall through.
                }
            }
        }
        return parameters;
    }
}
