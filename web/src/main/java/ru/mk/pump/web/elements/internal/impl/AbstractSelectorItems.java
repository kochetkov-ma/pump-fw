package ru.mk.pump.web.elements.internal.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.commons.utils.ParameterUtils;
import ru.mk.pump.commons.utils.Pre;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.SelectedItems;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.enums.SelectedStrategy;
import ru.mk.pump.web.elements.enums.StateType;
import ru.mk.pump.web.elements.internal.ActionFactory;
import ru.mk.pump.web.elements.internal.DocParameters;
import ru.mk.pump.web.elements.internal.SetState;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.SubElementsNotFoundException;
import ru.mk.pump.web.page.api.Page;

import java.util.List;

/**
 * {@inheritDoc}
 */
@SuppressWarnings("WeakerAccess")
@Slf4j
@DocParameters({"SELECTED_MARK",
        "SELECTED_STRATEGY"})
public abstract class AbstractSelectorItems extends AbstractWithItems implements SelectedItems {

    private static final String DEFAULT_SELECTED = "selected";

    private static final By[] DEFAULT_ITEMS_BY = {
            By.xpath("//option"),
            By.xpath("//div[contains(@class,'option') and not(contains(@class,'options'))]"),
            By.xpath("//div[contains(@class,'item') and not(contains(@class,'items'))]")
    };

    @Getter
    private final ActionFactory actionFactory = newDelegateActionFactory();

    @Getter(AccessLevel.PROTECTED)
    private String selectedCondition = DEFAULT_SELECTED;

    @Getter(AccessLevel.PROTECTED)
    private SelectedStrategy selectedStrategy = SelectedStrategy.CONTAINS;

    {
        setItemsBy(DEFAULT_ITEMS_BY);
    }

    public AbstractSelectorItems(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public AbstractSelectorItems(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public AbstractSelectorItems(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    public SetState ready() {
        return SetState
                .of(StateType.READY, super.ready(),
                        State.of(StateType.OTHER, () -> isNotEnoughChanging(CHANGING_TIMEOUT)).withName("Items are not changing enough"));
    }

    @Override
    public void set(Parameters params) {
        if (params.has(ElementParams.EDITABLE_SET_STRING)) {
            select(ParameterUtils.getOrNull(params, ElementParams.EDITABLE_SET_STRING.getName(), String.class));
            return;
        }
        if (params.has(ElementParams.EDITABLE_SET_NUMBER)) {
            //noinspection ConstantConditions
            select(ParameterUtils.getOrNull(params, ElementParams.EDITABLE_SET_NUMBER.getName(), Integer.class));
            return;
        }
        throw new IllegalArgumentException(String.format("Params map '%s' does not contains '%s'",
                Str.toString(params), Str.concat(ElementParams.EDITABLE_SET_NUMBER.getName(), ElementParams.EDITABLE_SET_STRING.getName())));
    }

    @Override
    public void select(String itemText) {
        final String finalText;
        if (itemText == null) {
            finalText = "";
        } else {
            finalText = itemText;
        }
        final List<Element> elements = getItems();
        final Action<?> action = actionFactory.newVoidAction(webElement -> elements.stream().filter(element -> find(element, finalText)).findFirst()
                .orElseThrow(() -> new SubElementsNotFoundException("text name = " + itemText).withElement(this))
                .click(), "Select item by text " + itemText).withStrategy(ActionStrategy.NO_AFTER, ActionStrategy.NO_FINALLY, ActionStrategy.NO_STATE_CHECK);
        getActionExecutor().execute(action);
    }

    @Override
    public void select(int index) {
        final List<Element> elements = getItems();
        Pre.checkArgListSize(index, elements);
        final Action<?> action = actionFactory.newVoidAction(w ->
                elements.get(index).click(), "Select item by index " + index)
                .withStrategy(ActionStrategy.NO_AFTER, ActionStrategy.NO_FINALLY, ActionStrategy.NO_STATE_CHECK);
        getActionExecutor().execute(action);
    }

    @Override
    public Element getSelected() {
        return getItems().stream().filter(i -> StringUtils.contains(i.getAttribute("class"), selectedCondition)).findFirst()
                .orElseThrow(
                        () -> new SubElementsNotFoundException("selected element class contains : " + selectedCondition).withElement(this));
    }

    @Override
    public String getItemsTextFast() {
        refreshItemsCache();
        final List<Element> items = getItemsCache();
        if (items.isEmpty()) {
            return "";
        }
        return items.get(0).getSubElements(Element.class).find(By.xpath("/../")).getTextHidden();
    }

    @Override
    protected void initFromParams() {
        super.initFromParams();
        selectedCondition = ParameterUtils.getOrDefault(getParams(), ElementParams.SELECTED_MARK.getName(), Parameter::getStringValue, selectedCondition);
        selectedStrategy = ParameterUtils.getOrDefault(getParams(), ElementParams.SELECTED_STRATEGY.getName(), SelectedStrategy.class, selectedStrategy);
    }

    protected boolean find(Element element, @NonNull String text) {
        final String elText = element.getTextHidden();
        if (selectedStrategy == SelectedStrategy.CONTAINS) {
            return StringUtils.containsIgnoreCase(elText, text);
        } else if (selectedStrategy == SelectedStrategy.EQUALS) {
            return text.equals(elText);
        } else {
            throw new UnsupportedOperationException("Strategy not support " + selectedStrategy.toString());
        }
    }
}