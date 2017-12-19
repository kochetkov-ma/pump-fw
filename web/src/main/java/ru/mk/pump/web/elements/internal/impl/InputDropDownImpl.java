package ru.mk.pump.web.elements.internal.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.ListUtils;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.api.concrete.InputDropDown;
import ru.mk.pump.web.elements.api.part.SelectedItems;
import ru.mk.pump.web.elements.api.part.Text;
import ru.mk.pump.web.elements.enums.StateType;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.elements.utils.Parameters;
import ru.mk.pump.web.page.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
public class InputDropDownImpl extends BaseElement implements InputDropDown {

    public final static By[] DEFAULT_LOAD_ICON = {};

    public final static By[] DEFAULT_INPUT_BY = {};

    public final static By[] DEFAULT_DROP_DOWN_BY = {};

    private By[] inputBy = DEFAULT_INPUT_BY;

    private By[] dropDownBy = DEFAULT_DROP_DOWN_BY;

    private By[] loadIconBy = DEFAULT_LOAD_ICON;

    private Element loadIcon;

    private DropDown dropDown;

    private Input input;

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
    public String type(String... text) {
        final List<String> oldItems = getItemsText(getItems());
        final String res = getInput().type(text);
        if (loadIconBy != null) {
            getLoadIcon().isDisplayed();
            getLoadIcon().isNotDisplayed();
        }
        if (!isChanged(oldItems)) {
            getReporter().error(String.format("InputDropDown items did not changed after type text '%s'", Strings.toString(text)),
                "ITEMS : " + Strings.toPrettyString(oldItems) + System.lineSeparator() + Strings.toPrettyString(getInfo()));
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
                    String.format("Parameter '%s' is not instance of String.class or Integer.class", params.get(ElementParams.EDITABLE_SET)));
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
                String.format("Parameter '%s' is not instance of String.class or Integer.class", params.get(ElementParams.EDITABLE_SET)));
        }
        throw new IllegalArgumentException(String.format("Params map '%s' does not contains '%s'", Strings.toString(params), ElementParams.EDITABLE_SET));
    }

    @Override
    protected void initFromParams() {
        super.initFromParams();
        inputBy = Parameters.getOrDefault(getParams(), ElementParams.INPUTDROPDOWN_INPUT_BY, By[].class, inputBy);
        dropDownBy = Parameters.getOrDefault(getParams(), ElementParams.INPUTDROPDOWN_DROPDOWN_BY, By[].class, dropDownBy);
        loadIconBy = Parameters.getOrDefault(getParams(), ElementParams.INPUTDROPDOWN_LOAD_BY, By[].class, loadIconBy);
    }

    protected Element getLoadIcon() {
        if (loadIcon == null) {
            loadIcon = getSubElements(Element.class).find(loadIconBy);
        }
        return loadIcon;
    }

    protected Input getInput() {
        if (input == null) {
            input = getSubElements(Input.class).find(inputBy);
            ((BaseElement) input).withParams(getParams());
        }
        return input;
    }

    protected DropDown getDropDown() {
        if (dropDown == null) {
            dropDown = getSubElements(DropDown.class).find(dropDownBy);
            ((DropDownImpl) dropDown).setStaticItems(false);
            ((BaseElement) dropDown).withParams(getParams());
        }
        return dropDown;
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
    }

    @Override
    public void select(int index) {
        getDropDown().select(index);
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

    protected boolean isChanged(List<String> oldItems) {
        return getStateResolver().resolve(itemsIsChangedOrEmpty(oldItems)).result().isSuccess();
    }

    protected State itemsIsChangedOrEmpty(List<String> oldItems) {
        return State.of(StateType.OTHER, () -> !ListUtils.isEqualList(getItemsText(getItems()), oldItems));
    }

    private List<String> getItemsText(List<Element> items) {
        return items.stream().map(Text::getTextHidden).collect(Collectors.toList());
    }
}
