package ru.mk.pump.web.elements.api.concrete.complex;

import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.Input;

public interface InputDropDown extends Input, DropDown {

    void typeAndSelect(String inputText, String dropDownItem);

    void typeAndSelect(String inputAndDropDownText);
}
