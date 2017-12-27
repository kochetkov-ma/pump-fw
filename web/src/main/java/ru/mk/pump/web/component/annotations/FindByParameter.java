package ru.mk.pump.web.component.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.component.internal.AbstractPumpParameterBuilder;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ComponentParameterBuilder(FindByParameter.PFindByParameterBuilder.class)
public @interface FindByParameter {

    String name();

    FindBy[] value();

    public static class PFindByParameterBuilder extends AbstractPumpParameterBuilder<By[], FindByParameter> {


        @Override
        protected String getName(FindByParameter annotation) {
            return annotation.name();
        }

        @Override
        protected Class<By[]> getClass(FindByParameter annotation) {
            return null;
        }

        @Override
        public By[] apply(String s) {
            return new By[0];
        }
    }


}
