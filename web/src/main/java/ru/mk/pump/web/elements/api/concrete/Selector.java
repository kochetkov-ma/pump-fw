package ru.mk.pump.web.elements.api.concrete;

import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.part.SelectedItems;
import ru.mk.pump.web.elements.internal.DocParameters;

@DocParameters({
    ElementParams.SELECTOR_STATIC_ITEMS,
    ElementParams.SELECTOR_ITEMS_BY,
    ElementParams.SELECTED_MARK,
    ElementParams.SELECTED_STRATEGY,
    ElementParams.EDITABLE_SET
})
public interface Selector extends SelectedItems {

}
