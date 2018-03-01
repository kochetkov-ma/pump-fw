package ru.mk.pump.web.elements.api.concrete;

import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.SelectedItems;
import ru.mk.pump.web.elements.internal.DocParameters;

@DocParameters({
    "DROPDOWN_EXPAND_BY",
    "DROPDOWN_BEFORE_SELECT",
    "SELECTOR_STATIC_ITEMS",
    "SELECTOR_ITEMS_BY",
    "SELECTED_MARK",
    "SELECTED_STRATEGY",
    "EDITABLE_SET"
})
public interface DropDown extends Element, SelectedItems {

    boolean isExpand();

    boolean isNotExpand();

    SelectedItems expand();
}
