package ru.mk.pump.web.elements.internal.interfaces;

import java.util.Optional;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.internal.*;
import ru.mk.pump.web.page.api.Page;

public interface InternalElement extends ElementInfo, ElementAction, ElementState, Enumerated, StrictInfo {

    Page getPage();

    Optional<InternalElement> getParent();

    Browser getBrowser();

    Finder getFinder();

    ElementWaiter getWaiter();

    ActionExecutor getActionExecutor();

    StateResolver getStateResolver();

    ActionsStore getActionsStore();

    boolean highlight(boolean enable);
}
