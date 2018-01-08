package ru.mk.pump.web.component.annotations;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.component.internal.ParameterTransformer;

import java.lang.annotation.*;

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
            return null;
        }
    }


}
