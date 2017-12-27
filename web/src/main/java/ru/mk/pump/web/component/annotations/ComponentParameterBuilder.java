package ru.mk.pump.web.component.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import ru.mk.pump.web.component.internal.AbstractPumpParameterBuilder;

@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentParameterBuilder {

    Class<? extends AbstractPumpParameterBuilder> value();
}
