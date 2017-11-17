package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.ElementWaiter;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.AbstractElement;
import ru.mk.pump.web.elements.internal.InternalElement;

public class BaseElement extends AbstractElement implements Element {


    public BaseElement(By avatarBy, Browser browser) {
        super(avatarBy, browser, new ElementWaiter());
    }

    public BaseElement(By avatarBy, BaseElement parent) {
        super(avatarBy, parent, new ElementWaiter());
    }

    @Override
    public String getText() {
        return getActionExecutor().execute(getTextAction());
    }

    @Override
    public void click() {
        getActionExecutor().execute(getClickAction());
    }

    @Override
    public BaseElement setIndex(int index) {
        super.setIndex(index);
        return this;

    }
}
