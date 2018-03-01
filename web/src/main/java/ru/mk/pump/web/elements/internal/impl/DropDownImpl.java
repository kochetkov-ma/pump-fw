package ru.mk.pump.web.elements.internal.impl;

import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.SelectedItems;
import ru.mk.pump.web.elements.internal.DocParameters;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.commons.utils.ParameterUtils;

/**
 * {@inheritDoc}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
@DocParameters({"DROPDOWN_EXPAND_BY",
        "DROPDOWN_BEFORE_SELECT"})
class DropDownImpl extends AbstractSelectorItems implements DropDown {

    private static final By[] DEFAULT_EXPAND_BY = {By.xpath("//i[contains(@class,chevron)]"), By.xpath(".")};

    private By[] expandBy = DEFAULT_EXPAND_BY;

    private Clickable expandButton = null;

    private Boolean beforeSelect = true;

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
        expandBy = ParameterUtils.getOrDefault(getParams(), ElementParams.DROPDOWN_EXPAND_BY.getName(), By[].class, expandBy);
        beforeSelect = ParameterUtils.getOrDefault(getParams(), ElementParams.DROPDOWN_BEFORE_SELECT.getName(), Boolean.class, beforeSelect);
    }

    @Override
    public void select(String itemText) {
        if (beforeSelect) {
            beforeSelect();
        }
        super.select(itemText);
    }

    @Override
    public void select(int index) {
        if (beforeSelect) {
            beforeSelect();
        }
        super.select(index);
    }

    @Override
    public boolean isExpand() {
        return !getItems().isEmpty() && getItems().get(0).isDisplayed(1000).result().isSuccess();
    }

    @Override
    public boolean isNotExpand() {
        return getItems().isEmpty() || getItems().get(0).isNotDisplayed(1000).result().isSuccess();
    }

    @Override
    public SelectedItems expand() {
        if (isNotExpand()) {
            getExpandButton().click();
        }
        return this;
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