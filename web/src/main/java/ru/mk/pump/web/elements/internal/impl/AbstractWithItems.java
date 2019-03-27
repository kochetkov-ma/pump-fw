package ru.mk.pump.web.elements.internal.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import ru.mk.pump.commons.utils.ParameterUtils;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.WithSubItems;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.enums.StateType;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.DocParameters;
import ru.mk.pump.web.elements.internal.SetState;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
@DocParameters({"SELECTOR_ITEMS_BY",
        "SELECTOR_STATIC_ITEMS"})
public abstract class AbstractWithItems extends BaseElement implements WithSubItems {

    protected static final int CHANGING_TIMEOUT = ConfigurationHolder.get().getElement().getSubItemsChangingTimeout() * 1000;
    private static final int CHANGING_DELAY = ConfigurationHolder.get().getElement().getSubItemsChangingPoolDelay();
    private By itemsByCache;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private Boolean staticItems = true;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private By[] itemsBy;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    protected List<Element> itemsCache = null;

    public AbstractWithItems(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public AbstractWithItems(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public AbstractWithItems(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    public List<Element> getItems() {
        Action<List<Element>> action = newDelegateActionFactory()
                .newAction((el) -> {
                    if (!getStaticItems() || itemsCache == null || itemsCache.isEmpty()) {
                        getWaiter().withTimeoutMs(CHANGING_TIMEOUT).withTimeoutMs(CHANGING_DELAY)
                                .waitPredicate(this::refreshItemsCache, Objects::equals);
                    }
                    return itemsCache;
                }, "Get sub-elements list").withStrategy(ActionStrategy.SIMPLE);
        return getActionExecutor().execute(action);
    }

    @Override
    public WaitResult<Boolean> hasItems(int count, int timeoutMs) {
        final SetState state = (SetState) SetState.of(StateType.OTHER, exists(), State.of(StateType.EXISTS, () -> refreshItemsCache() >= count))
                .withName(String.format("Items count not less '%d'", count));
        return getInternalStateResolver().resolve(state, timeoutMs).result();
    }

    @Override
    public WaitResult<Boolean> hasItems(int count) {
        final SetState state = (SetState) SetState.of(StateType.OTHER, exists(), State.of(StateType.EXISTS, () -> refreshItemsCache() >= count))
                .withName(String.format("Items count not less '%d'", count));
        return getInternalStateResolver().resolve(state).result();
    }

    @SuppressWarnings("SameParameterValue")
    public boolean isNotEnoughChanging(int timeoutMs) {
        return getWaiter().withTimeoutMs(timeoutMs).withDelay(CHANGING_DELAY).waitPredicate(this::getItemsTextFast, StringUtils::equalsIgnoreCase).isSuccess();
    }

    @SuppressWarnings("SameParameterValue")
    public boolean isNotEnoughChanging(int timeoutMs, int delayMs) {
        return getWaiter().withTimeoutMs(timeoutMs).withDelay(delayMs).waitPredicate(this::getItemsTextFast, StringUtils::equalsIgnoreCase).isSuccess();
    }

    public int refreshItemsCache() {
        if (itemsByCache == null) {
            itemsCache = getSubElements(Element.class).findList(getItemsBy());
            if (!itemsCache.isEmpty()) {
                itemsByCache = itemsCache.get(0).advanced().getBy();
            }
        } else {
            itemsCache = getSubElements(Element.class).findList(itemsByCache);
        }
        return itemsCache.size();
    }

    /**
     * [RUS] Ускоренное получение текста всех под-элементов.
     * Ведется поиск общего родительского элемента для элементов списка и читается его полный текст
     */
    abstract public String getItemsTextFast();

    @Override
    protected void initFromParams() {
        super.initFromParams();
        setStaticItems(ParameterUtils.getOrDefault(getParams(), ElementParams.SELECTOR_STATIC_ITEMS.getName(), Boolean.class, getStaticItems()));
        setItemsBy(ParameterUtils.getOrDefault(getParams(), ElementParams.SELECTOR_ITEMS_BY.getName(), By[].class, getItemsBy()));
    }
}