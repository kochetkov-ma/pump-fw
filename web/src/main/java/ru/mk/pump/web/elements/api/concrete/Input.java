package ru.mk.pump.web.elements.api.concrete;

import java.util.Map;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.Editable;

public interface Input extends Element, Editable {

    String type(String... text);

    @Override
    default void set(Parameters params) {
        //noinspection ConstantConditions
        type(params.getOrEmpty(ElementParams.EDITABLE_SET_STRING).getStringValue());
    }
}
