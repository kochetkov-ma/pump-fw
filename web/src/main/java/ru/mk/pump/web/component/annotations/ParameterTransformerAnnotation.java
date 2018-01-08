package ru.mk.pump.web.component.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import ru.mk.pump.web.component.internal.ParameterTransformer;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterTransformerAnnotation {

    Class<? extends ParameterTransformer> value();
}
