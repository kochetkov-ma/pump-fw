package ru.mk.pump.web.elements.internal.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.commons.utils.ParameterUtils;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Complex;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.api.concrete.complex.Child;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.api.part.SelectedItems;
import ru.mk.pump.web.elements.enums.StateType;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.DocParameters;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementException;
import ru.mk.pump.web.page.api.Page;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
@Slf4j
@FrameworkImpl
@DocParameters({
        "EXTRA_INPUT_BY",
        "INPUTDROPDOWN_DROPDOWN_BY",
        "DROPDOWN_LOAD_BY",
        "DROPDOWN_EXPAND_BY",
        "DROPDOWN_BEFORE_SELECT",
        "SELECTOR_STATIC_ITEMS",
        "SELECTOR_ITEMS_BY",
        "SELECTED_MARK",
        "SELECTED_STRATEGY"
})
class InputDropDownImpl extends BaseElement implements InputDropDown, Complex {

    public final static By[] DEFAULT_INPUT_BY = {By.tagName("input")};

    public final static By[] DEFAULT_DROP_DOWN_BY = {By.xpath(".")};

    private final Child<Input> input;

    private final Child<DropDown> dropDown;

    public InputDropDownImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public InputDropDownImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public InputDropDownImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    public String toString() {
        return "InputDropDownImpl(" +
                "input=" + Strings.toString(input) +
                ", dropDown=" + Strings.toString(dropDown) +
                ") " + super.toString();
    }

    @Override
    public String type(String... text) {
        final String oldItems = ((DropDownImpl) getDropDown()).getItemsTextFast();
        final String res = getInput().type(text);
        ((DropDownImpl) getDropDown()).checkLoadIconFlow();
        if (!isChanged(oldItems)) {
            getReporter().warn(String.format("InputDropDown items did not changed after type text '%s'", Strings.toString(text)),
                    "OLD ITEMS : " + oldItems + System.lineSeparator() + "CURRENT ITEMS : " + ((DropDownImpl) getDropDown()).getItemsTextFast()
                            + System.lineSeparator() + Strings.toPrettyString(getInfo()));
        }
        return res;
    }

    /**
     * @param params required - {@link ElementParams#EDITABLE_SET_STRING} <br/>
     *               optional - {@link ElementParams#INPUT_DROPDOWN_SET_STRING} <br/>
     *               optional - {@link ElementParams#INPUT_DROPDOWN_SET_NUMBER} <br/>
     *               if optional param is absent then use required param in INPUT and DROP_DOWN select
     */
    @Override
    public void set(Parameters params) {
        if (params.has(ElementParams.INPUT_DROPDOWN_SET_STRING)) {
            typeAndSelect(params.getOrEmpty(ElementParams.EDITABLE_SET_STRING.getName()).getStringValue(),
                    ParameterUtils.getOrNull(params, ElementParams.INPUT_DROPDOWN_SET_STRING.getName(), String.class));
            return;
        }
        if (params.has(ElementParams.INPUT_DROPDOWN_SET_NUMBER)) {
            type(params.getOrEmpty(ElementParams.EDITABLE_SET_STRING.getName()).getStringValue());
            //noinspection ConstantConditions
            select(ParameterUtils.getOrNull(params, ElementParams.INPUT_DROPDOWN_SET_NUMBER.getName(), Integer.class));
            return;
        }
        typeAndSelect(params.getOrEmpty(ElementParams.EDITABLE_SET_STRING.getName()).getStringValue());
    }

    @Override
    public String getText() {
        return getInput().getText();
    }

    @Override
    public void clear() {
        getInput().clear();
    }

    @Override
    public String getTextHidden() {
        return getInput().getTextHidden();
    }

    @Override
    public Map<String, String> getInfo() {
        final Map<String, String> res = super.getInfo();
        res.put("drop down", Strings.toString(dropDown));
        res.put("input", Strings.toString(input));
        return res;

    }

    @Override
    public boolean isExpand() {
        return getDropDown().isExpand();
    }

    @Override
    public boolean isNotExpand() {
        return getDropDown().isNotExpand();
    }

    @Override
    public SelectedItems expand() {
        return getDropDown().expand();
    }

    @Override
    public void select(String itemText) {
        getDropDown().select(itemText);
        checkItemsDisappear();
    }

    @Override
    public void select(int index) {
        getDropDown().select(index);
        checkItemsDisappear();
    }

    @Override
    public Element getSelected() {
        return getDropDown().getSelected();
    }

    @Override
    public List<Element> getItems() {
        return getDropDown().getItems();
    }

    @Override
    public WaitResult<Boolean> hasItems(int count, int timeoutMs) {
        return getDropDown().hasItems(count, timeoutMs);
    }

    @Override
    public WaitResult<Boolean> hasItems(int count) {
        return getDropDown().hasItems(count);
    }

    @Override
    public boolean isNotEnoughChanging(int timeoutMs) {
        return getDropDown().isNotEnoughChanging(timeoutMs);
    }

    @Override
    public boolean isNotEnoughChanging(int timeoutMs, int delayMs) {
        return getDropDown().isNotEnoughChanging(timeoutMs, delayMs);
    }

    public boolean isItemsDisappear() {
        final DropDownImpl dropDown = (DropDownImpl) getDropDown();
        if (dropDown.getItemsCache().isEmpty()) {
            return true;
        }
        final Element element = dropDown.getItemsCache().get(0);
        return element.isNotDisplayed().result().isSuccess();
    }

    @Override
    public void typeAndSelect(String inputText, String dropDownItem) {
        /*type input*/
        type(inputText);
        /*select item*/
        select(dropDownItem);
    }

    @Override
    public void typeAndSelect(String inputAndDropDownText) {
        typeAndSelect(inputAndDropDownText, inputAndDropDownText);
    }

    protected Input getInput() {
        //noinspection UnnecessaryLocalVariable
        final Input res = input.get(Input.class);
        return res;
    }

    protected DropDown getDropDown() {
        final DropDown res = dropDown.get(DropDown.class);
        ((DropDownImpl) res).setStaticItems(false);
        Parameters params = Parameters.of(ElementParams.DROPDOWN_BEFORE_SELECT.withValue(false),
                getParams().get(ElementParams.DROPDOWN_EXPAND_BY),
                getParams().get(ElementParams.DROPDOWN_LOAD_BY)

        );
        ((BaseElement) res)
                .withParams(params);

        return res;
    }

    protected boolean isChanged(String oldItems) {
        return getStateResolver().resolve(itemsIsChangedOrEmpty(oldItems), 1000).result().isSuccess();
    }



    protected State itemsIsChangedOrEmpty(String oldItems) {
        return State.of(StateType.OTHER, () -> !StringUtils.equalsIgnoreCase(((DropDownImpl) getDropDown()).getItemsTextFast(), oldItems));
    }

    private void checkItemsDisappear() {
        if (!isItemsDisappear()) {
            throw new ElementException("Items did not disappeared after selection").withTargetElement(this);
        }
    }

    {
        input = new Child<Input>(this, ElementParams.EXTRA_INPUT_BY.getName()).withDefaultBy(DEFAULT_INPUT_BY);
        dropDown = new Child<DropDown>(this, ElementParams.INPUTDROPDOWN_DROPDOWN_BY.getName()).withDefaultBy(DEFAULT_DROP_DOWN_BY);
    }
}