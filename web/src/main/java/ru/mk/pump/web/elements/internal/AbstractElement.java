package ru.mk.pump.web.elements.internal;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.api.listeners.ActionListener;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.enums.StateType;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.elements.utils.Xpath;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue", "unchecked"})
abstract class AbstractElement<CHILD> implements InternalElement {

    /*turn off for more speed*/
    public static boolean JS_WAIT = false;

    private final By avatarBy;

    private final ElementWaiter waiter;

    @Getter
    private final Browser browser;

    private final InternalElement parentElement;

    @Getter
    private final ActionExecutor actionExecutor;

    @Getter
    private final StateResolver stateResolver;

    @Getter
    private final Finder finder;

    private final Consumer<WaitResult<Boolean>> TEAR_DOWN = stateWaitResult -> getFinder().getLast()
        .ifPresent(waitResult -> stateWaitResult.withCause(waitResult.getCause()));

    @Getter
    private final ActionsStore actionsStore;

    @Setter(AccessLevel.PROTECTED)
    protected boolean xpathAutoFix = true;

    private String elementName = "empty (strongly recommend to add)";

    @Getter
    private String elementDescription = "empty (recommend to add)";

    private final Page page;

    private int listIndex = -1;

    //region CONSTRUCTORS
    public AbstractElement(By avatarBy, Page page) {
        this(avatarBy, null, null, page);
    }

    public AbstractElement(By avatarBy, InternalElement parentElement) {
        this(avatarBy, parentElement, null, null);
    }

    public AbstractElement(By avatarBy, Browser browser) {
        this(avatarBy, null, browser, null);
    }

    private AbstractElement(@NotNull By avatarBy, @Nullable InternalElement parentElement, @Nullable Browser browser, @Nullable Page page) {
        this.avatarBy = xpathAutoFix ? Xpath.fixIfXpath(avatarBy) : avatarBy;
        this.parentElement = parentElement;
        if (parentElement != null) {
            this.page = parentElement.getPage();
            this.browser = parentElement.getBrowser();
        } else {
            this.page = page;
            if (page != null) {
                this.browser = page.getBrowser();
            } else {
                this.browser = browser;
            }
        }
        this.waiter = newDelegateWaiter();
        this.finder = newDelegateFinder();
        this.stateResolver = newDelegateStateResolver();
        this.actionsStore = new ActionsStore(this, newDelegateActionFactory());
        this.actionExecutor = newDelegateActionExecutor(stateResolver);
    }

    //endregion

    //region delegates
    protected ElementWaiter newDelegateWaiter() {
        return ElementWaiter.newWaiterS();
    }

    protected Finder newDelegateFinder() {
        return new Finder(this);
    }

    protected ActionFactory newDelegateActionFactory() {
        return new ActionFactory(this);
    }

    protected StateResolver newDelegateStateResolver() {
        return new StateResolver(this);
    }

    protected ActionExecutor newDelegateActionExecutor(StateResolver stateResolver) {
        return new ActionExecutor().withStateResolver(stateResolver);
    }

    public AbstractElement addActionListener(List<ActionListener> actionListener) {
        actionListener.forEach(actionExecutor::addListener);
        return this;
    }
    //endregion

    public CHILD withName(String elementName) {
        if (!Strings.isEmpty(elementName)) {
            this.elementName = elementName;
        }
        return (CHILD) this;
    }

    public CHILD withDescription(String elementDescription) {
        if (!Strings.isEmpty(elementDescription)) {
            this.elementDescription = elementDescription;
        }
        return (CHILD) this;
    }

    @Override
    public String getName() {
        return elementName;
    }

    @Override
    public By getBy() {
        return avatarBy;
    }

    @Override
    public Page getPage() {
        return page;
    }

    @Override
    public Optional<InternalElement> getParent() {
        return Optional.ofNullable(parentElement);
    }

    @Override
    public ElementWaiter getWaiter() {
        return waiter;
    }

    @Override
    public boolean isList() {
        return listIndex != -1;
    }

    @Override
    public int getIndex() {
        return listIndex;
    }

    @Override
    public InternalElement setIndex(int index) {
        if (!isList()) {
            getFinder().setFindStrategy(new ListElementStrategy(this));
        }
        listIndex = index;
        return this;
    }

    @Override
    public Action<String> getClickAction() {
        return actionsStore.clickAction();
    }

    @Override
    public Action<String> getTextAction() {
        return actionsStore.textAction();
    }

    @Override
    public Action<?> getClearAction() {
        return actionsStore.clear();
    }

    @Override
    public Action<?> getFocusAction() {
        return actionsStore.focusAction();
    }

    @Override
    public Action<String> getInputAction(CharSequence... keys) {
        return actionsStore.inputAction(keys);
    }

    public String getAttribute(String name) {
        return actionExecutor
            .execute(actionsStore.attribute(name).withStrategy(ActionStrategy.NO_FINALLY, ActionStrategy.NO_AFTER).redefineExpectedState(exists()));
    }

    public State jsReady() {
        final String js = "return document.readyState";
        return State.of(StateType.OTHER, () -> "complete".equals(Strings.toString(getBrowser().actions().executeScript(js))), TEAR_DOWN)
            .withName("JS is completed");
    }

    @Override
    public State notExists() {
        getFinder().clearCache();
        return State.of(StateType.EXISTS.not(), () -> !getFinder().findFast().isSuccess(), TEAR_DOWN).withName("Not Exists in DOM")
            .withName("Not Exists Or Not Displayed");
    }

    @Override
    public SetState notDisplayed() {
        return (SetState) SetState.of(StateType.DISPLAYED.not(), notExists(), State.of(StateType.SELENIUM_DISPLAYED.not(), () -> {
            final WaitResult<WebElement> res = getFinder().findFast();
            return !res.isSuccess() || !res.getResult().isDisplayed();
        }, TEAR_DOWN)).withName("Not Exists Or Not Displayed");
    }

    @Override
    public SetState notEnabled() {
        return (SetState) SetState.of(StateType.ENABLED.not(), notExists(), State.of(StateType.SELENIUM_ENABLED.not(), () -> {
            final WaitResult<WebElement> res = getFinder().findFast();
            return !res.isSuccess() || !res.getResult().isEnabled();
        }, TEAR_DOWN)).withName("Not Exists Or Not Enabled");
    }

    @Override
    public SetState exists() {
        getFinder().clearCache();
        if (JS_WAIT) {
            return (SetState) SetState.of(StateType.EXISTS, jsReady(),
                State.of(StateType.SELENIUM_EXISTS, () -> getFinder().findFast().isSuccess(), TEAR_DOWN)).withTearDown(TEAR_DOWN).withName("Exists in DOM");
        } else {
            return (SetState) SetState.of(StateType.EXISTS,
                State.of(StateType.SELENIUM_EXISTS, () -> getFinder().findFast().isSuccess(), TEAR_DOWN)).withTearDown(TEAR_DOWN).withName("Exists in DOM");
        }
    }

    @Override
    public SetState displayed() {
        return (SetState) SetState.of(StateType.DISPLAYED, exists(), State.of(StateType.SELENIUM_DISPLAYED, () -> {
            final WaitResult<WebElement> res = getFinder().findFast();
            return res.isSuccess() && res.getResult().isDisplayed();
        }, TEAR_DOWN)).withName("Exists And Displayed");
    }


    @Override
    public SetState enabled() {
        return (SetState) SetState.of(StateType.ENABLED, exists(), State.of(StateType.SELENIUM_ENABLED, () -> {
            final WaitResult<WebElement> res = getFinder().findFast();
            return res.isSuccess() && res.getResult().isEnabled();
        }, TEAR_DOWN)).withName("Exists And Enabled");
    }

    @Override
    public SetState ready() {
        return (SetState) SetState.of(StateType.READY, displayed(), enabled()).withName("Ready to interact");
    }

    @Override
    public SetState clearState() {
        return (SetState) SetState.of(StateType.OTHER, exists(), State.of(StateType.OTHER, () -> {
            final WaitResult<WebElement> res = getFinder().findFast();
            return res.isSuccess() && Strings.isEmpty(getBrowser().actions().getText(res.getResult()));
        }, TEAR_DOWN)).withName("Text of the element became an empty");
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PumpElement(");
        sb.append("type=").append(getClass().getSimpleName());
        sb.append(", avatarBy=").append(avatarBy);
        if (page == null) {
            sb.append(", browser=").append(browser.getConfig().getType());
        }
        if (parentElement != null) {
            sb.append(", parentElement=").append(parentElement.getClass().getSimpleName()).append(":").append(parentElement.getBy());
        }
        sb.append(", elementName='").append(elementName).append('\'');
        sb.append(", elementDescription='").append(elementDescription).append('\'');
        if (page != null) {
            sb.append(", page=").append(page);
        }
        if (isList()) {
            sb.append(", listIndex=").append(listIndex);
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public Map<String, String> getInfo() {
        final LinkedHashMap<String, String> result = Maps.newLinkedHashMap();
        result.put("type", getClass().getSimpleName());
        if (isList()) {
            result.put("list index", String.valueOf(getIndex()));
        } else {
            result.put("list element", "no");
        }
        result.put("name", getName());
        result.put("description", getElementDescription());
        result.put("by", getBy().toString());
        if (parentElement != null) {
            result.put("parentElement", Strings.space(parentElement.getName(), parentElement.getBy().toString()));
        } else {
            result.put("parentElement", "no");
        }
        if (page != null) {
            result.put("page", Strings.space(page.getName(), page.getUrl()));
        } else {
            result.put("page", "no");
        }
        result.put("browser", getBrowser().getId());
        return result;
    }
}
