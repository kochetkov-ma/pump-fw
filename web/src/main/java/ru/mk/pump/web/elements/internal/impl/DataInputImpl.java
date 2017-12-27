package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.annotations.Date;
import ru.mk.pump.web.elements.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
@Date
class DataInputImpl extends BaseElement implements Input {

    public DataInputImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public DataInputImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public DataInputImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    public String type(String... text) {
        return null;
    }
}