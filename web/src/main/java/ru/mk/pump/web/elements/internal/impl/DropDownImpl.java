package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.SelectedItems;
import ru.mk.pump.web.elements.enums.StateType;
import ru.mk.pump.web.elements.internal.SetState;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.elements.utils.Parameters;
import ru.mk.pump.web.page.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
class DropDownImpl extends AbstractSelectorItems implements DropDown {

    private static final By[] DEFAULT_EXPAND_BY = {By.xpath("//i[contains(@class,chevron)]"), By.xpath(".")};

    private By[] expandBy = DEFAULT_EXPAND_BY;

    private Clickable expandButton = null;

    public DropDownImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public DropDownImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public DropDownImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    protected void initFromParams() {
        super.initFromParams();
        expandBy = Parameters.getOrDefault(getParams(), ElementParams.DROPDOWN_EXPAND_BY, By[].class, expandBy);
    }

    @Override
    public void select(String itemText) {
        beforeSelect();
        super.select(itemText);
    }

    @Override
    public void select(int index) {
        beforeSelect();
        super.select(index);
    }

    @Override
    public boolean isExpand() {
        return getStateResolver().resolve(expandState()).result().getResult();
    }

    @Override
    public boolean isNotExpand() {
        return getItems().isEmpty() || getItems().get(0).isNotDisplayed();
    }

    @Override
    public SelectedItems expand() {
        if (isNotExpand()) {
            getExpandButton().click();
        }
        return this;
    }

    protected SetState expandState() {
        return SetState.of(StateType.OTHER, State.of(StateType.OTHER, () -> !getItems().isEmpty()), displayed());
    }

    protected Clickable getExpandButton() {
        if (expandButton == null) {
            expandButton = getSubElements(Element.class).find(expandBy);
        }
        return expandButton;
    }

    protected void beforeSelect() {
        if (isNotExpand()) {
            expand();
        }
    }
}