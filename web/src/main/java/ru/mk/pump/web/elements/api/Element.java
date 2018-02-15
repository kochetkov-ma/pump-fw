package ru.mk.pump.web.elements.api;

import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.common.api.PageItem;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.Text;
import ru.mk.pump.web.elements.internal.DocParameters;
import ru.mk.pump.web.elements.internal.SubElementHelper;
import ru.mk.pump.web.elements.internal.interfaces.ElementInfo;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;


@DocParameters({
    ElementParams.FOCUS_CUSTOM_SCRIPT,
    ElementParams.FOCUS_TYPE,
    ElementParams.CLEAR_TYPE,
})
public interface Element extends PrettyPrinter, StrictInfo, Clickable, Text, PageItem {

    ElementInfo info();

    InternalElement advanced();

    boolean isDisplayed();

    boolean isNotDisplayed();

    boolean isExists();

    boolean isNotExists();

    boolean isEnabled();

    boolean isNotEnabled();

    boolean isDisplayed(int timeoutMs);

    boolean isNotDisplayed(int timeoutMs);

    boolean isExists(int timeoutMs);

    boolean isNotExists(int timeoutMs);

    String getAttribute(String name);

    <T extends Element> SubElementHelper<T> getSubElements(Class<T> subElementInterfaceClazz);
}
