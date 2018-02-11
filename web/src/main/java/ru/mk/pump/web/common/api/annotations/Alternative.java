package ru.mk.pump.web.common.api.annotations;

import ru.mk.pump.web.elements.api.annotations.Requirements;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Requirements
public @interface Alternative {
}
