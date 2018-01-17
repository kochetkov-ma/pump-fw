package ru.mk.pump.web.common.api;

import java.lang.annotation.Annotation;
import java.util.Map;
import ru.mk.pump.commons.activity.Parameter;

public interface ParameterTransformer<T, A extends Annotation> {

    Map<String, Parameter<T>> transform(A annotation);
}
