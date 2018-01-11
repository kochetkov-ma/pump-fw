package ru.mk.pump.web.common.pageobject.annotations;

import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.common.pageobject.ParameterTransformer;

import java.lang.annotation.*;
import ru.mk.pump.web.common.pageobject.ParameterTransformerAnnotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ParameterTransformerAnnotation(PString.StringParameterTransformer.class)
public @interface PString {

    String name();

    String value();

    class StringParameterTransformer extends ParameterTransformer<String, PString> {

        @Override
        public String getName(PString annotation) {
            return annotation.name();
        }

        @Override
        public Parameter<String> getParameter(PString annotation) {
            return Parameter.of(annotation.value());
        }
    }

}
