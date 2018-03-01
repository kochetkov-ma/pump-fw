package ru.mk.pump.web.common.api.annotations;

import com.google.common.collect.Maps;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Map;
import org.openqa.selenium.By;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.web.common.api.ParameterTransformer;
import ru.mk.pump.web.common.api.annotations.PFindBy.PFindByTransformer;
import ru.mk.pump.web.common.pageobject.ParameterTransformerAnnotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@ParameterTransformerAnnotation(PFindBys.PFindBysTransformer.class)
public @interface PFindBys {

    PFindBy[] value();

    class PFindBysTransformer implements ParameterTransformer<By[], PFindBys> {

        @Override
        public Parameters transform(PFindBys annotation) {
            final ParameterTransformer<By[], PFindBy> transformer = new PFindByTransformer();
            final Parameters result = Parameters.of();
            Arrays.stream(annotation.value())
                .forEach(a -> result.addAll(transformer.transform(a)));
            return result;
        }
    }
}
