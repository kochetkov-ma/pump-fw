package ru.mk.pump.web.elements.api;

import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.Text;
import ru.mk.pump.web.elements.internal.SubElementHelper;

public interface Element extends PrettyPrinter, StrictInfo, Clickable, Text {

    boolean isDisplayed();

    boolean isNotDisplayed();

    <T extends Element> SubElementHelper<T> getSubElements(Class<T> subElementClazz);

}
