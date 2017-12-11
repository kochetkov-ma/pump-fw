package ru.mk.pump.web.elements.internal.impl;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.Selector;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;
import ru.mk.pump.web.page.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
@FrameworkImpl
class SelectorImpl extends BaseElement implements Selector {

    private List<Element> itemsCache = null;

    private String DEFAULT_SELECTED = "selected";

    private By DEFAULT_ITEMS_BY = By.xpath("div[@class='options']");

    private By itemsBy = DEFAULT_ITEMS_BY;

    private String selectedCondition = DEFAULT_SELECTED;

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
        selectedCondition = getOrNull(BY_PARAM_NAME, Parameter::asString);
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

    }

    @Override
    public void select(int index) {

    }

    @Override
    public Element getSelected() {
        return getItems().stream().filter(i -> StringUtils.contains(i.getAttribute("class"), selectedCondition)).findFirst()
            .orElseThrow(
                () -> new ElementFinderNotFoundException("Cannot find selected element by class contains " + selectedCondition).withTargetElement(this));
    }

    @Override
    public List<Element> getItems() {
        if (itemsCache == null) {
            refreshItemsCache();
        }
        return itemsCache;
    }

    @Override
    public void set(Map<String, Parameter<?>> params) {

    }

    private List<Element> refreshItemsCache() {
        itemsCache = getSubElements(Element.class).findList(itemsBy);
        return itemsCache;
    }
}