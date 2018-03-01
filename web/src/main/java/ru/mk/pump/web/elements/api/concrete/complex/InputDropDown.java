package ru.mk.pump.web.elements.api.concrete.complex;

import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.internal.DocParameters;

@DocParameters({
    "INPUTDROPDOWN_INPUT_BY",
    "INPUTDROPDOWN_DROPDOWN_BY",
    "INPUTDROPDOWN_LOAD_BY",
    "DROPDOWN_EXPAND_BY",
    "DROPDOWN_BEFORE_SELECT",
    "SELECTOR_STATIC_ITEMS",
    "SELECTOR_ITEMS_BY",
    "SELECTED_MARK",
    "SELECTED_STRATEGY",
    "EDITABLE_SET"
})
public interface InputDropDown extends Input, DropDown {

    void typeAndSelect(String inputText, String dropDownItem);

    void typeAndSelect(String inputAndDropDownText);
}
