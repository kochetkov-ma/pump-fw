package ru.mk.pump.web.elements.api.concrete;

import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.SelectedItems;

public interface DropDown extends Element, SelectedItems {

    boolean isExpand();

    void collapse();

    SelectedItems expand();
}
