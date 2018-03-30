package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.api.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.Frame;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
class FrameImpl extends BaseElement implements Frame {

    public FrameImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public FrameImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public FrameImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    public void switchTo() {
        Action<?> action = newDelegateActionFactory()
            .newVoidAction((el) -> getBrowser().actions().switchToFrame(el), "Switch to frame");
        getActionExecutor().execute(action);
    }
}