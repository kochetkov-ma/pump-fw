package ru.mk.pump.web.common.api.annotations;

import com.google.common.collect.Maps;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Map;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.common.api.ParameterTransformer;
import ru.mk.pump.web.common.api.annotations.PString.StringParameterTransformer;
import ru.mk.pump.web.common.pageobject.ParameterTransformerAnnotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@ParameterTransformerAnnotation(PStrings.PStringsTransformer.class)
public @interface PStrings {

    PString[] value();

    class PStringsTransformer implements ParameterTransformer<String, PStrings> {

        @Override
        public Map<String, Parameter<String>> transform(PStrings annotation) {
            final ParameterTransformer<String, PString> transformer = new StringParameterTransformer();
            final Map<String, Parameter<String>> result = Maps.newHashMap();
            Arrays.stream(annotation.value())
                .forEach(a -> result.putAll(transformer.transform(a)));
            return result;
        }
    }
}
