package ru.mk.pump.web.elements.internal.impl;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.Selector;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
class SelectorImpl extends BaseElement implements Selector {

    public SelectorImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public SelectorImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public SelectorImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    public void select(String itemText) {

    }

    @Override
    public void select(int index) {

    }

    @Override
    public Element getSelected() {
        return null;
    }

    @Override
    public List<Element> getItems() {
        return null;
    }

    @Override
    public void set(Map<String, Parameter<?>> params) {

    }
}