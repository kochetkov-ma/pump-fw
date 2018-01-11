package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.api.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.Selector;
import ru.mk.pump.web.elements.internal.ActionFactory;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
class SelectorImpl extends AbstractSelectorItems implements Selector {

    private final ActionFactory actionFactory = newDelegateActionFactory();

    public SelectorImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public SelectorImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public SelectorImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }


}