package ru.mk.pump.web.elements.api.concrete;

import com.google.common.base.Preconditions;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.Editable;
import ru.mk.pump.web.elements.enums.CheckBoxState;
import ru.mk.pump.web.elements.internal.DocParameters;

import static ru.mk.pump.web.constants.ElementParams.EDITABLE_SET_CHECKBOX;

@SuppressWarnings({"UnnecessaryInterfaceModifier", "unused"})
@DocParameters({
        "EDITABLE_SET"
})
public interface CheckBox extends Element, Editable {

    CheckBoxState getState();

    void setState(CheckBoxState state);

    @Override
    default void set(Parameters params) {
        Preconditions.checkArgument(params.has(EDITABLE_SET_CHECKBOX), "Has EDITABLE_SET_CHECKBOX in Parameters");
        setState(params.get(EDITABLE_SET_CHECKBOX).getValue(CheckBoxState.class));
    }
}
