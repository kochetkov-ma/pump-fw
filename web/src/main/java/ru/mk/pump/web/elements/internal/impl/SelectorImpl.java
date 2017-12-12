package ru.mk.pump.web.elements.internal.impl;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.Selector;
import ru.mk.pump.web.elements.api.part.Editable;
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

@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
class SelectorImpl extends BaseElement implements Selector {

    public static final String SELECTED_PARAM_NAME = "selectedCondition";

    public static final String SELECTED_STRATEGY_PARAM_NAME = "selectedStrategy";

    private static final String DEFAULT_SELECTED = "selected";

    private static final By DEFAULT_ITEMS_BY = By.xpath("div[contains(@class,'option')]");

    private List<Element> itemsCache = null;

    private ActionFactory actionFactory = newDelegateActionFactory();

    private By itemsBy = DEFAULT_ITEMS_BY;

    private String selectedCondition = DEFAULT_SELECTED;

    private SelectedStrategy selectedStrategy = SelectedStrategy.CONTAINS;

    public SelectorImpl(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public SelectorImpl(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public SelectorImpl(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    protected void initFromParams() {
        super.initFromParams();
        selectedCondition = Parameters.getOrNull(getParams(), SELECTED_PARAM_NAME, Parameter::asString);
        selectedStrategy = Parameters.getOrNull(getParams(), SELECTED_STRATEGY_PARAM_NAME, SelectedStrategy.class);
    }

    @Override
    protected By getExtraBy() {
        if (super.getExtraBy() == null) {
            return DEFAULT_ITEMS_BY;
        } else {
            return super.getExtraBy();
        }

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
        }, "Select item by text " + itemText).withStrategy(ActionStrategy.SIMPLE);
        getActionExecutor().execute(action);
    }

    @Override
    public void select(int index) {
        final List<Element> elements = getItems();
        Preconditions.checkArgListSize(index, elements);
        final Action<String> action = actionFactory.newAction(w -> {
            elements.get(index).click();
        }, "Select item by index " + index).withStrategy(ActionStrategy.SIMPLE);
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
        if (itemsCache == null) {
            refreshItemsCache();
        }
        return itemsCache;
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
        if (params.containsKey(Editable.PARAM_NAME)) {
            if (params.get(Editable.PARAM_NAME).isClass(String.class)) {
                select(Parameters.getOrNull(params, Editable.PARAM_NAME, String.class));
            }
            if (params.get(Editable.PARAM_NAME).isClass(Integer.class)) {
                //noinspection ConstantConditions
                select(Parameters.getOrNull(params, Editable.PARAM_NAME, Integer.class));
            }
        }
    }

    private void refreshItemsCache() {
        itemsCache = getSubElements(Element.class).findList(itemsBy);
    }
}