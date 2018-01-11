package ru.mk.pump.web.common.pageobject;

import com.google.common.collect.Maps;
import ru.mk.pump.commons.activity.Parameter;

import java.lang.annotation.Annotation;
import java.util.Map;

public abstract class ParameterTransformer<T, A extends Annotation> {

    protected abstract String getName(A annotation);

    protected abstract Parameter<T> getParameter(A annotation);

    Map.Entry<String, Parameter<T>> transform(A annotation) {
        return Maps.immutableEntry(getName(annotation), getParameter(annotation));
    }
}
