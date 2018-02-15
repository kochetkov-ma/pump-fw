package ru.mk.pump.web.elements.api.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Requirements Marker. Custom element implementation from external app. It is more important than {@link FrameworkImpl}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Requirements
public @interface CustomImpl {

}
