package ru.mk.pump.web.elements.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker of annotation. Defined requirement annotation to parse same element type
 */
@SuppressWarnings("ALL")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Requirements {

    String value() default "";
}
