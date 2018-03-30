package ru.mk.pump.web.elements.internal;

import org.openqa.selenium.WebElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

public class FindFrame extends SingleElementStrategy {

    FindFrame(InternalElement internalElement) {
        super(internalElement);
    }

    @Override
    public WebElement findSelf() {
        WebElement res = super.findSelf();
        getTarget().getBrowser().actions().switchToFrame(res);
        return null;
    }
}
