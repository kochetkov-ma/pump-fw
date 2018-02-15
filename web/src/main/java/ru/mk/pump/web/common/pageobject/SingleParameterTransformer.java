package ru.mk.pump.web.common.pageobject;

import com.google.common.collect.ImmutableMap;
import java.lang.annotation.Annotation;
import java.util.Map;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.common.api.ParameterTransformer;

public abstract class SingleParameterTransformer<T, A extends Annotation> implements ParameterTransformer<T,A> {

    protected abstract String getName(A annotation);

    protected abstract Parameter<T> getParameter(A annotation);

    @Override
    public Map<String, Parameter<T>> transform(A annotation) {
        return ImmutableMap.of(getName(annotation), getParameter(annotation));
    }
}
