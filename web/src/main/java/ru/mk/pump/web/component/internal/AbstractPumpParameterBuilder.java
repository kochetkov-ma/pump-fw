package ru.mk.pump.web.component.internal;

import java.lang.annotation.Annotation;
import java.util.function.Function;
import ru.mk.pump.commons.activity.Parameter;

public abstract class AbstractPumpParameterBuilder<T, A extends Annotation> implements Function<String, T> {

    protected abstract String getName(A annotation);

    protected abstract Object getValue(A annotation);

    protected abstract Class<T> getClass(A annotation);

    public Parameter<T> buildIt(A annotation) {
        return Parameter.of(this, getClass(annotation));
    }
}
