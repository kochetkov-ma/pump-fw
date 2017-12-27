package ru.mk.pump.web.component.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.mk.pump.web.component.internal.AbstractPumpParameterBuilder;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ComponentParameterBuilder(StringParameter.StringParameterBuilder.class)
public @interface StringParameter {

    String name();

    String value();

    public static class StringParameterBuilder extends AbstractPumpParameterBuilder<String, StringParameter> {

        @Override
        protected String getName(StringParameter annotation) {
            return annotation.name();
        }

        @Override
        protected Class<String> getClass(StringParameter annotation) {
            return String.class;
        }

        @Override
        public String apply(String s) {
            return s;
        }
    }

}
