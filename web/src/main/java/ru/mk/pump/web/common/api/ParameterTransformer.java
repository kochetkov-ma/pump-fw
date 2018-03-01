package ru.mk.pump.web.common.api;

import ru.mk.pump.commons.helpers.Parameters;

import java.lang.annotation.Annotation;

public interface ParameterTransformer<T, A extends Annotation> {

    Parameters transform(A annotation);
}
