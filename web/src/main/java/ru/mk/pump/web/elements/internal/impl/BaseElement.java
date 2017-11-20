package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.AbstractElement;
import ru.mk.pump.web.elements.internal.ElementWaiter;
import ru.mk.pump.web.elements.internal.SetState;

@Slf4j
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
    public boolean isDisplayed() {
        final SetState res = getStateResolver().resolve(displayed());
        log.info(StringConstants.LINE + Strings.mapToPrettyString(res.getInfo()));
        return res.result().orElseThrow(this::onEmptyResult).isSuccess();
    }

    @Override
    public boolean isNotDisplayed() {
        final SetState res = getStateResolver().resolve(notDisplayed());
        log.info(StringConstants.LINE + Strings.mapToPrettyString(res.getInfo()));
        return res.result().orElseThrow(this::onEmptyResult).isSuccess();
    }

    @Override
    public BaseElement setIndex(int index) {
        super.setIndex(index);
        return this;
    }

    private PumpException onEmptyResult() {
        return new PumpException("Empty result");
    }
}
