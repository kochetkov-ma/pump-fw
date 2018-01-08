package ru.mk.pump.web.component.internal;

import com.google.common.collect.Maps;
import ru.mk.pump.commons.activity.Parameter;

import java.lang.annotation.Annotation;
import java.util.Map;

public abstract class ParameterTransformer<T, A extends Annotation> {

    protected abstract String getName(A annotation);

    protected abstract Parameter<T> getParameter(A annotation);

    public Map.Entry<String, Parameter<T>> trasform(A annotation) {
        return Maps.immutableEntry(getName(annotation), getParameter(annotation));
    }
}
