package ru.mk.pump.web.elements.internal;

import java.util.Optional;
import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.page.Page;

public interface InternalElement extends ElementAction, ElementState, MultiElement {

    String getName();

    By getBy();

    Optional<Page> getPage();

    Optional<InternalElement> getParent();

    Browser getBrowser();

    Finder getFinder();

    ElementWaiter getWaiter();
}
