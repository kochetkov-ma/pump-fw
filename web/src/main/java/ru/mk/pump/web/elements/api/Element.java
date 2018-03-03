package ru.mk.pump.web.elements.api;

import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.common.api.PageItem;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.Text;
import ru.mk.pump.web.elements.internal.DocParameters;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.elements.internal.SubElementHelper;
import ru.mk.pump.web.elements.internal.interfaces.ElementInfo;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;


@SuppressWarnings("unused")
@DocParameters({
        "FOCUS_CUSTOM_SCRIPT",
        "FOCUS_TYPE",
        "CLEAR_TYPE",
})
public interface Element extends PrettyPrinter, StrictInfo, Clickable, Text, PageItem {

    ElementInfo info();

    InternalElement advanced();

    State isDisplayed();

    State isNotDisplayed();

    State isExists();

    State isNotExists();

    State isEnabled();

    State isNotEnabled();

    State isDisplayed(int timeoutMs);

    State isNotDisplayed(int timeoutMs);

    State isExists(int timeoutMs);

    State isNotExists(int timeoutMs);

    String getAttribute(String name);

    String getTagName();

    <T extends Element> SubElementHelper<T> getSubElements(Class<T> subElementInterfaceClazz);
}
