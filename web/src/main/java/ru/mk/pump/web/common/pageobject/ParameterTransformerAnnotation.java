package ru.mk.pump.web.common.pageobject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterTransformerAnnotation {

    Class<? extends ParameterTransformer> value();
}
