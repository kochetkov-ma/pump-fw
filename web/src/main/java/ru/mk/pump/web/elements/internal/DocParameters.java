package ru.mk.pump.web.elements.internal;

import java.lang.annotation.*;

/**
 * See this parameters in {@link ru.mk.pump.web.constants.ElementParams}
 */
@Documented
@Inherited
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface DocParameters {

    String[] value();
}
