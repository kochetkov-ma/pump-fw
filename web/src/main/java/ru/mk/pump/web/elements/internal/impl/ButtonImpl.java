package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.internal.BaseElement;

class ButtonImpl extends BaseElement implements Button {

    public ButtonImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    public ButtonImpl(By avatarBy, BaseElement parent) {
        super(avatarBy, parent);
    }
}