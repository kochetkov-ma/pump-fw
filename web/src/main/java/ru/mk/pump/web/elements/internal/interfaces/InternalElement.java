package ru.mk.pump.web.elements.internal.interfaces;

import java.util.Optional;
import org.openqa.selenium.By;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.internal.ElementWaiter;
import ru.mk.pump.web.elements.internal.Finder;
import ru.mk.pump.web.page.api.Page;

public interface InternalElement extends ElementAction, ElementState, Enumerated, StrictInfo {

    String getName();

    By getBy();

    Page getPage();

    Optional<InternalElement> getParent();

    Browser getBrowser();

    Finder getFinder();

    ElementWaiter getWaiter();
}
