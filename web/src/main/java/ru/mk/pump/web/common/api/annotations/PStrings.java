package ru.mk.pump.web.common.api.annotations;

import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.web.common.api.ParameterTransformer;
import ru.mk.pump.web.common.api.annotations.PString.StringParameterTransformer;
import ru.mk.pump.web.common.pageobject.ParameterTransformerAnnotation;

import java.lang.annotation.*;
import java.util.Arrays;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@ParameterTransformerAnnotation(PStrings.PStringsTransformer.class)
public @interface PStrings {

    class PStringsTransformer implements ParameterTransformer<String, PStrings> {

        @Override
        public Parameters transform(PStrings annotation) {
            final ParameterTransformer<String, PString> transformer = new StringParameterTransformer();
            final Parameters result = Parameters.of();
            Arrays.stream(annotation.value())
                    .forEach(a -> result.addAll(transformer.transform(a)));
            return result;
        }
    }

    PString[] value();
}
