package ru.mk.pump.web.elements.internal.impl;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
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
import ru.mk.pump.web.utils.Parameters;
import ru.mk.pump.web.exceptions.ElementException;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
@Slf4j
@FrameworkImpl
@DocParameters({
    ElementParams.INPUTDROPDOWN_INPUT_BY,
    ElementParams.INPUTDROPDOWN_DROPDOWN_BY,
    ElementParams.INPUTDROPDOWN_LOAD_BY,
    ElementParams.DROPDOWN_EXPAND_BY,
    ElementParams.DROPDOWN_BEFORE_SELECT,
    ElementParams.SELECTOR_STATIC_ITEMS,
    ElementParams.SELECTOR_ITEMS_BY,
    ElementParams.SELECTED_MARK,
    ElementParams.SELECTED_STRATEGY
})
class InputDropDownImpl extends BaseElement implements InputDropDown, Complex {

    public final static By[] DEFAULT_LOAD_ICON = {};

    public final static By[] DEFAULT_INPUT_BY = {By.tagName("input")};

    public final static By[] DEFAULT_DROP_DOWN_BY = {By.xpath(".")};

    private final Child<Input> input;

    private final Child<DropDown> dropDown;

    private final Child<Element> loadIcon;

    {
        input = new Child<Input>(ElementParams.INPUTDROPDOWN_INPUT_BY, this).withDefaultBy(DEFAULT_INPUT_BY);
        dropDown = new Child<DropDown>(ElementParams.INPUTDROPDOWN_DROPDOWN_BY, this).withDefaultBy(DEFAULT_DROP_DOWN_BY);
        loadIcon = new Child<>(ElementParams.INPUTDROPDOWN_LOAD_BY, this).withDefaultBy(DEFAULT_LOAD_ICON);
    }

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
            ", loadIcon=" + Strings.toString(loadIcon) +
            ") " + super.toString();
    }

    @Override
    public String type(String... text) {
        final String oldItems = ((DropDownImpl) getDropDown()).getItemsTextFast();
        final String res = getInput().type(text);
        checkLoadIconFlow(loadIcon);
        if (!isChanged(oldItems)) {
            getReporter().warn(String.format("InputDropDown items did not changed after type text '%s'", Strings.toString(text)),
                "OLD ITEMS : " + oldItems + System.lineSeparator() + "CURRENT ITEMS : " + ((DropDownImpl) getDropDown()).getItemsTextFast()
                    + System.lineSeparator() + Strings.toPrettyString(getInfo()));
        }
        return res;
    }

    /**
     * @param params
     * required - {@link ElementParams#EDITABLE_SET} <br/>
     * optional - {@link ElementParams#INPUT_DROPDOWN_SET} <br/>
     * if optional param is absent then use required param in INPUT and DROP_DOWN select
     */
    @Override
    public void set(Map<String, Parameter<?>> params) {
        if (!params.containsKey(ElementParams.INPUT_DROPDOWN_SET)) {
            if (params.containsKey(ElementParams.EDITABLE_SET)) {
                if (params.get(ElementParams.EDITABLE_SET).isClass(String.class)) {
                    typeAndSelect(Parameters.getOrNull(params, ElementParams.EDITABLE_SET, String.class));
                    return;
                }
                throw new IllegalArgumentException(
                    String.format("FindByParameter '%s' is not instance of String.class or Integer.class", params.get(ElementParams.EDITABLE_SET)));
            }
        } else {
            if (params.get(ElementParams.INPUT_DROPDOWN_SET).isClass(String.class)) {
                typeAndSelect(Parameters.getOrNull(params, ElementParams.EDITABLE_SET, String.class),
                    Parameters.getOrNull(params, ElementParams.INPUT_DROPDOWN_SET, String.class));
                return;
            }
            if (params.get(ElementParams.INPUT_DROPDOWN_SET).isClass(Integer.class)) {
                type(Parameters.getOrNull(params, ElementParams.EDITABLE_SET, String.class));
                //noinspection ConstantConditions
                select(Parameters.getOrNull(params, ElementParams.INPUT_DROPDOWN_SET, Integer.class));
                return;
            }
            throw new IllegalArgumentException(
                String.format("FindByParameter '%s' is not instance of String.class or Integer.class", params.get(ElementParams.EDITABLE_SET)));
        }
        throw new IllegalArgumentException(String.format("Params map '%s' does not contains '%s'", Strings.toString(params), ElementParams.EDITABLE_SET));
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
        res.put("drop down", Strings.toPrettyString(getDropDown().getInfo(), "drop down".length()));
        res.put("input", Strings.toPrettyString(getInput().getInfo(), "input".length()));
        res.put("load icon", Strings.toString(loadIcon));
        return res;

    }

    protected Input getInput() {
        //noinspection UnnecessaryLocalVariable
        final Input res = input.get(Input.class);
        //TODO::Удалить?
        /* Безконтрольное использование параметров, которые предназначены только для главного элемента
        ((BaseElement) res).withParams(getParams());
        */
        return res;
    }

    protected DropDown getDropDown() {
        final DropDown res = dropDown.get(DropDown.class);
        ((DropDownImpl) res).setStaticItems(false);
        ((BaseElement) res)
            //TODO::Удалить?
            /* Безконтрольное использование параметров, которые предназначены только для главного элемента
            .withParams(getParams())
            */
            .withParams(ImmutableMap.of("beforeSelect", Parameter.of(Boolean.class, false)));
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

    public boolean isItemsDisappear() {
        final DropDownImpl dropDown = (DropDownImpl) getDropDown();
        if (dropDown.getItemsCache().isEmpty()) {
            return true;
        }
        final Element element = dropDown.getItemsCache().get(0);
        return element.isNotDisplayed();
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

    protected boolean isChanged(String oldItems) {
        return getStateResolver().resolve(itemsIsChangedOrEmpty(oldItems), 1000).result().isSuccess();
    }

    protected void checkLoadIconFlow(Child<Element> loadIcon) {
        //TODO:Сделать настраиваемым
        if (loadIcon.isDefined()) {
            final Element element = loadIcon.get(Element.class);
            /*строгая проверка*/
            element.advanced().getStateResolver().resolve(element.advanced().displayed(), 1).result().throwExceptionOnFail();
            element.advanced().getStateResolver().resolve(element.advanced().notDisplayed(), 1).result().throwExceptionOnFail();
            /*НЕ строгая проверка*/
            /*
            loadIcon.get(Element.class).isDisplayed(1);
            loadIcon.get(Element.class).isNotDisplayed(1);
            */
        }
    }

    protected State itemsIsChangedOrEmpty(String oldItems) {
        return State.of(StateType.OTHER, () -> !StringUtils.equalsIgnoreCase(((DropDownImpl) getDropDown()).getItemsTextFast(), oldItems));
    }

    private void checkItemsDisappear() {
        if (!isItemsDisappear()) {
            throw new ElementException("Items did not disappeared after selection").withTargetElement(this);
        }
    }
}