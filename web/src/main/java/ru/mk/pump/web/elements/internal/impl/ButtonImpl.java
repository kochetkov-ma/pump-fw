package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
class ButtonImpl extends BaseElement implements Button {

    public ButtonImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public ButtonImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public ButtonImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }
}