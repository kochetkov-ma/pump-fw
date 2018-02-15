package ru.mk.pump.web.common.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.common.pageobject.ParameterTransformerAnnotation;
import ru.mk.pump.web.common.pageobject.SingleParameterTransformer;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@ParameterTransformerAnnotation(PString.StringParameterTransformer.class)
public @interface PString {

    String name();

    String value();

    class StringParameterTransformer extends SingleParameterTransformer<String, PString> {

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
