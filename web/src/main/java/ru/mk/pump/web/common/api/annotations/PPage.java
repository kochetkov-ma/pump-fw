package ru.mk.pump.web.common.api.annotations;

import static ru.mk.pump.commons.constants.StringConstants.UNDEFINED;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.mk.pump.commons.constants.StringConstants;

@SuppressWarnings("ALL")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PPage {

    String value() default "";

    String desc() default "";

    String baseUrl() default "";

    String resource() default "";


}
