package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import ru.mk.pump.commons.utils.ParameterUtils;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.complex.Child;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.SelectedItems;
import ru.mk.pump.web.elements.enums.StateType;
import ru.mk.pump.web.elements.internal.DocParameters;
import ru.mk.pump.web.elements.internal.SetState;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

import java.util.Map;

/**
 * {@inheritDoc}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
@DocParameters({"DROPDOWN_EXPAND_BY",
        "DROPDOWN_BEFORE_SELECT"})
@Slf4j
class DropDownImpl extends AbstractSelectorItems implements DropDown {

    public final static By[] DEFAULT_LOAD_ICON = {};

    private Child<Element> loadIcon;

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

    protected Child<Element> getLoadIcon() {
        if (loadIcon == null) {
            loadIcon = new Child<>(this, ElementParams.DROPDOWN_LOAD_BY.getName()).withDefaultBy(DEFAULT_LOAD_ICON);
        }
        return loadIcon;
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

    /*
        {
            getActionExecutor().addBefore(getActionFactory().newVoidAction(this::checkLoadIconFlow, "Load icon check").setMaxTruCount(1).withStrategy(
                ActionStrategy.SIMPLE, ActionStrategy.NO_STATE_CHECK));

        }
    */
    @Override
    public Map<String, String> getInfo() {
        final Map<String, String> res = super.getInfo();
        res.put("load icon", Strings.toString(loadIcon));
        return res;

    }

    @Override
    public SetState ready() {
        return SetState
                .of(StateType.READY,
                        State.of(StateType.OTHER, this::isDisappearLoadIcon).withName("Load icon Disappear"),
                        super.ready());
    }

    boolean isDisappearLoadIcon() {
        if (getLoadIcon().isDefined()) {
            final Element element = getLoadIcon().get(Element.class);
            boolean res = element.isNotDisplayed().result().isSuccess();
            return res;
        }
        return true;
    }

    void checkLoadIconFlow() {
        if (getLoadIcon().isDefined()) {
            final Element element = getLoadIcon().get(Element.class);
            //TODO: May be 101
            element.advanced().getStateResolver().resolve(element.advanced().displayed(), 100, 100).result();
            element.advanced().getStateResolver().resolve(element.advanced().notDisplayed(), 5000, 100).result().throwExceptionOnFail();
        }
    }

}