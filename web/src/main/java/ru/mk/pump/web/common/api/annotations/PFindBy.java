package ru.mk.pump.web.common.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBy.FindByBuilder;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.common.pageobject.ParameterTransformerAnnotation;
import ru.mk.pump.web.common.pageobject.SingleParameterTransformer;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@ParameterTransformerAnnotation(PFindBy.PFindByTransformer.class)
public @interface PFindBy {

    String name();

    FindBy[] value();

    class PFindByTransformer extends SingleParameterTransformer<By[], PFindBy> {

        @Override
        public String getName(PFindBy annotation) {
            return annotation.name();
        }

        @Override
        public Parameter<By[]> getParameter(PFindBy annotation) {
            final FindByBuilder builder = new FindByBuilder();
            return Parameter.of(By[].class, Arrays.stream(annotation.value()).map(i -> builder.buildIt(i, null)).toArray(By[]::new));
        }
    }
}
