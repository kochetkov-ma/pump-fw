package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.elements.api.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.TextArea;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@FrameworkImpl
class TextAreaImpl extends BaseElement implements TextArea {

    public TextAreaImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public TextAreaImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public TextAreaImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }
}
