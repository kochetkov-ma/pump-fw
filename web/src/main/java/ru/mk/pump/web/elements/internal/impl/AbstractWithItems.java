package ru.mk.pump.web.elements.internal.impl;

import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.browsers.Browser;
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
import ru.mk.pump.web.utils.Parameters;

@SuppressWarnings("WeakerAccess")
@DocParameters({ElementParams.SELECTOR_ITEMS_BY, ElementParams.SELECTOR_STATIC_ITEMS})
public abstract class AbstractWithItems extends BaseElement implements WithSubItems {

    private static final int CHANGING_DELAY = ConfigurationHolder.get().getElement().getSubItemsChangingPoolDelay();

    protected static final int CHANGING_TIMEOUT = ConfigurationHolder.get().getElement().getSubItemsChangingTimeout();

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
    protected void initFromParams() {
        super.initFromParams();
        setStaticItems(Parameters.getOrDefault(getParams(), ElementParams.SELECTOR_STATIC_ITEMS, Boolean.class, getStaticItems()));
        setItemsBy(Parameters.getOrDefault(getParams(), ElementParams.SELECTOR_ITEMS_BY, By[].class, getItemsBy()));
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
            }, "Get sub-elements list").withStrategy(ActionStrategy.NO_STATE_CHECK, ActionStrategy.SIMPLE);
        return getActionExecutor().execute(action);
    }

    @Override
    public WaitResult<Boolean> hasItems(int count, int timeoutMs) {
        final SetState state = (SetState) SetState.of(StateType.OTHER, exists(), State.of(StateType.EXISTS, () -> getItems().size() >= count))
            .withName(String.format("Items count not less '%d'", count));
        return getStateResolver().resolve(state, timeoutMs).result();
    }

    @Override
    public WaitResult<Boolean> hasItems(int count) {
        final SetState state = (SetState) SetState.of(StateType.OTHER, exists(), State.of(StateType.EXISTS, () -> getItems().size() >= count))
            .withName(String.format("Items count not less '%d'", count));
        return getStateResolver().resolve(state).result();
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

    @SuppressWarnings("SameParameterValue")
    public boolean isNotEnoughChanging(int timeoutMs) {
        return getWaiter().withTimeoutMs(timeoutMs).withDelay(CHANGING_DELAY).waitPredicate(this::getItemsTextFast, StringUtils::equalsIgnoreCase).isSuccess();
    }

    @SuppressWarnings("SameParameterValue")
    public boolean isNotEnoughChanging(int timeoutMs, int delayMs) {
        return getWaiter().withTimeoutMs(timeoutMs).withDelay(delayMs).waitPredicate(this::getItemsTextFast, StringUtils::equalsIgnoreCase).isSuccess();
    }

    /**
     * [RUS] Ускоренное получение текста всех под-элементов.
     * Ведется поиск общего родительского элемента для элементов списка и читается его полный текст
     */
    abstract public String getItemsTextFast();
}
