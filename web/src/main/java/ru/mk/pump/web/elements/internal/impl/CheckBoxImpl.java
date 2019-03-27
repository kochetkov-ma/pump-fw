package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.CheckBox;
import ru.mk.pump.web.elements.api.concrete.complex.Child;
import ru.mk.pump.web.elements.enums.CheckBoxState;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"unused", "WeakerAccess"})
@FrameworkImpl
public class CheckBoxImpl extends BaseElement implements CheckBox {

    private final static By[] DEFAULT_INPUT_BY = {By.tagName("input")};

    private Child<Element> input;

    public CheckBoxImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public CheckBoxImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public CheckBoxImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    public CheckBoxState getState() {
        Action<Boolean> action = newDelegateActionFactory().newAction((webElement) ->
            getInput().advanced().getActionExecutor().execute(getInput().advanced().getActionsStore().selected())
        , "Check state");

        if (getActionExecutor().execute(action)) {
            return CheckBoxState.CHECKED;
        } else {
            return CheckBoxState.UNCHECKED;
        }
    }

    @Override
    public void setState(CheckBoxState state) {
        if (getState() != state) {
            click();
        }
    }

    private Element getInput() {
        /*исходный элемент уже input*/
        if ("input".equals(getTagName())) {
            return this;
        }
        /*инициализировать Child<Element> для поиска и хранения input*/
        if (input == null) {
            input = new Child<>(this, ElementParams.EXTRA_INPUT_BY.getName()).withDefaultBy(DEFAULT_INPUT_BY);
        }
        return input.get(Element.class);
    }
}
