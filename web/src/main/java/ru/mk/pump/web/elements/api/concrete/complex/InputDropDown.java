package ru.mk.pump.web.elements.api.concrete.complex;

import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.internal.DocParameters;

@DocParameters({
    ElementParams.INPUTDROPDOWN_INPUT_BY,
    ElementParams.INPUTDROPDOWN_DROPDOWN_BY,
    ElementParams.INPUTDROPDOWN_LOAD_BY,
    ElementParams.DROPDOWN_EXPAND_BY,
    ElementParams.DROPDOWN_BEFORE_SELECT,
    ElementParams.SELECTOR_STATIC_ITEMS,
    ElementParams.SELECTOR_ITEMS_BY,
    ElementParams.SELECTED_MARK,
    ElementParams.SELECTED_STRATEGY,
    ElementParams.EDITABLE_SET
})
public interface InputDropDown extends Input, DropDown {

    void typeAndSelect(String inputText, String dropDownItem);

    void typeAndSelect(String inputAndDropDownText);
}