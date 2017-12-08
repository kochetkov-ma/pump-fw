package ru.mk.pump.web.elements.api;

import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.ElementInfo;
import ru.mk.pump.web.elements.api.part.Text;
import ru.mk.pump.web.elements.internal.SubElementHelper;

public interface Element extends PrettyPrinter, StrictInfo, Clickable, Text, ElementInfo {

    boolean isDisplayed();

    boolean isNotDisplayed();

    boolean isExists();

    boolean isNotExists();

    boolean isEnabled();

    boolean isNotEnabled();

    <T extends Element> SubElementHelper<T> getSubElements(Class<T> subElementInterfaceClazz);
}
