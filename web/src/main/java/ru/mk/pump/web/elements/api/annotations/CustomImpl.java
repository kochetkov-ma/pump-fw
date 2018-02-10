package ru.mk.pump.web.elements.api.annotations;


import java.lang.annotation.*;

/**
 * Requirements Marker. Custom element implementation from external app. It is more important than {@link FrameworkImpl}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Requirements
public @interface CustomImpl {

}
