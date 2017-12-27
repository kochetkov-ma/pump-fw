package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
@Slf4j
class InputImpl extends BaseElement implements Input {

    public InputImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public InputImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public InputImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    public String type(String... text) {
        return getActionExecutor().execute(getInputAction(text));
    }
}