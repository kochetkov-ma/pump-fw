package ru.mk.pump.web.common.pageobject.annotations;

import java.util.Arrays;
import org.openqa.selenium.By;
import org.openqa.selenium.support.AbstractFindByBuilder;
import org.openqa.selenium.support.FindAll.FindByBuilder;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.common.pageobject.ParameterTransformer;

import java.lang.annotation.*;
import ru.mk.pump.web.common.pageobject.ParameterTransformerAnnotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ParameterTransformerAnnotation(PFindBy.PFindByTransformer.class)
public @interface PFindBy {

    String name();

    FindBy[] value();

    class PFindByTransformer extends ParameterTransformer<By[], PFindBy> {

        @Override
        public String getName(PFindBy annotation) {
            return annotation.name();
        }

        @Override
        public Parameter<By[]> getParameter(PFindBy annotation) {
            final AbstractFindByBuilder builder = new FindByBuilder();
            return Parameter.of(By[].class, Arrays.stream(annotation.value()).map(i -> builder.buildIt(i,null)).toArray(By[]::new));
        }
    }
}
