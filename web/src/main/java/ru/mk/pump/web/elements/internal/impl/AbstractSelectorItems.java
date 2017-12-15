package ru.mk.pump.web.elements.internal.impl;

import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.Editable;
import ru.mk.pump.web.elements.api.part.SelectedItems;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.enums.SelectedStrategy;
import ru.mk.pump.web.elements.internal.ActionFactory;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.elements.utils.Parameters;
import ru.mk.pump.web.elements.utils.Preconditions;
import ru.mk.pump.web.exceptions.SubElementsNotFoundException;
import ru.mk.pump.web.page.Page;

@SuppressWarnings("WeakerAccess")
abstract class AbstractSelectorItems extends BaseElement implements SelectedItems {

    private static final String DEFAULT_SELECTED = "selected";

    private static final By[] DEFAULT_ITEMS_BY = {
        By.xpath("//option"),
        By.xpath("//div[contains(@class,'option') and not(contains(@class,'options'))]"),
        By.xpath("//div[contains(@class,'item') and not(contains(@class,'items'))]")
    };

    @Getter
    private final ActionFactory actionFactory = newDelegateActionFactory();

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    protected List<Element> itemsCache = null;

    @Getter(AccessLevel.PROTECTED)
    private String selectedCondition = DEFAULT_SELECTED;

    @Getter(AccessLevel.PROTECTED)
    private SelectedStrategy selectedStrategy = SelectedStrategy.CONTAINS;

    @Getter(AccessLevel.PROTECTED)
    private Boolean staticItems = true;

    {
        setExtraBy(DEFAULT_ITEMS_BY);
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
    protected void initFromParams() {
        super.initFromParams();
        selectedCondition = Parameters.getOrDefault(getParams(), ElementParams.SELECTED_CONDITION, Parameter::asString, selectedCondition);
        selectedStrategy = Parameters.getOrDefault(getParams(), ElementParams.SELECTED_STRATEGY, SelectedStrategy.class, selectedStrategy);
        staticItems = Parameters.getOrDefault(getParams(), ElementParams.STATIC_ITEMS, Boolean.class, staticItems);
    }

    protected boolean find(Element element, @NotNull String text) {
        final String elText = element.getTextHidden();
        if (selectedStrategy == SelectedStrategy.CONTAINS) {
            return StringUtils.containsIgnoreCase(elText, text);
        } else if (selectedStrategy == SelectedStrategy.EQUALS) {
            return text.equals(elText);
        } else {
            throw new UnsupportedOperationException("Strategy not support " + selectedStrategy.toString());
        }
    }

    @Override
    public void set(Map<String, Parameter<?>> params) {
        if (params.containsKey(ElementParams.EDITABLE_SET)) {
            if (params.get(ElementParams.EDITABLE_SET).isClass(String.class)) {
                select(Parameters.getOrNull(params, ElementParams.EDITABLE_SET, String.class));
                return;
            }
            if (params.get(ElementParams.EDITABLE_SET).isClass(Integer.class)) {
                //noinspection ConstantConditions
                select(Parameters.getOrNull(params, ElementParams.EDITABLE_SET, Integer.class));
                return;
            }
            throw new IllegalArgumentException(
                String.format("Parameter '%s' is not instance of String.class or Integer.class", params.get(ElementParams.EDITABLE_SET)));
        }
        throw new IllegalArgumentException(String.format("Params map '%s' does not contains '%s'", Strings.toString(params), ElementParams.EDITABLE_SET));
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
        final Action<String> action = actionFactory.newAction(webElement -> {
            elements.stream().filter(element -> find(element, finalText)).findFirst()
                .orElseThrow(() -> new SubElementsNotFoundException("text name = " + itemText).withTargetElement(this))
                .click();
        }, "Select item by text " + itemText).withStrategy(ActionStrategy.NO_AFTER, ActionStrategy.NO_FINALLY, ActionStrategy.NO_STATE_CHECK);
        getActionExecutor().execute(action);
    }

    @Override
    public void select(int index) {
        final List<Element> elements = getItems();
        Preconditions.checkArgListSize(index, elements);
        final Action<String> action = actionFactory.newAction(w -> {
            elements.get(index).click();
        }, "Select item by index " + index).withStrategy(ActionStrategy.NO_AFTER, ActionStrategy.NO_FINALLY,  ActionStrategy.NO_STATE_CHECK);
        getActionExecutor().execute(action);
    }

    @Override
    public Element getSelected() {
        return getItems().stream().filter(i -> StringUtils.contains(i.getAttribute("class"), selectedCondition)).findFirst()
            .orElseThrow(
                () -> new SubElementsNotFoundException("selected element class contains : " + selectedCondition).withTargetElement(this));
    }

    @Override
    public List<Element> getItems() {
        if (itemsCache == null ) {
            refreshItemsCache();
        }
        return itemsCache;
    }

    public void refreshItemsCache() {
        itemsCache = getSubElements(Element.class).findList(getExtraBy());
    }
}