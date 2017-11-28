package ru.mk.pump.web.elements.api.concrete;

import java.util.Map;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.Editable;

public interface Input extends Element, Editable {

    String set(String... text);

    @Override
    default void set(Map<String, Parameter<?>> params) {
        set(params.getOrDefault(PARAM_NAME, Parameter.of("undefined")).asString());
    }
}
