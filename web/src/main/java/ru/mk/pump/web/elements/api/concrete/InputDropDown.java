package ru.mk.pump.web.elements.api.concrete;

public interface InputDropDown extends Input, DropDown {

    void typeAndSelect(String inputText, String dropDownItem);

    void typeAndSelect(String inputAndDropDownText);
}
