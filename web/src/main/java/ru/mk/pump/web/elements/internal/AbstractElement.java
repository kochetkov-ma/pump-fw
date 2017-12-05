package ru.mk.pump.web.elements.internal;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.api.listeners.ActionListener;
import ru.mk.pump.web.elements.internal.State.StateType;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.Page;

@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue", "unchecked"})
abstract class AbstractElement<CHILD> implements InternalElement {

    private final By avatarBy;

    private final ElementWaiter waiter;

    @Getter
    private final Browser browser;

    private final InternalElement parentElement;

    @Getter(AccessLevel.PROTECTED)
    private final ActionExecutor actionExecutor;

    @Getter
    private final StateResolver stateResolver;

    @Getter
    private final Finder finder;

    private final Consumer<WaitResult<Boolean>> TEAR_DOWN = stateWaitResult -> getFinder().getLast()
        .ifPresent(waitResult -> stateWaitResult.withCause(waitResult.getCause()));

    private final ActionsStore actionsStore;

    private String elementName = "empty (strongly recommend to add)";

    @Getter
    private String elementDescription = "empty (recommend to add)";

    private Page page;

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
        this.avatarBy = avatarBy;
        this.page = page;
        this.parentElement = parentElement;
        if (parentElement != null) {
            this.page = parentElement.getPage();
            this.browser = parentElement.getBrowser();
        } else {
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

    public State notExists() {
        return (State) State.of(() -> !getFinder().findFast().isSuccess(), StateType.EXISTS.not(), TEAR_DOWN).withName("Not Exists in DOM");
    }

    public SetState notDisplayed() {
        return (SetState) SetState.of(StateType.DISPLAYED.not(), notExists(), State.of(() -> {
            final WaitResult<WebElement> res = getFinder().findFast();
            return !res.isSuccess() || !res.getResult().isDisplayed();
        }, StateType.SELENIUM_DISPLAYED.not(), TEAR_DOWN)).withName("Not Exists Or Not Displayed");
    }

    public SetState notEnabled() {
        return (SetState) SetState.of(StateType.ENABLED.not(), notExists(), State.of(() -> {
            final WaitResult<WebElement> res = getFinder().findFast();
            return !res.isSuccess() || !res.getResult().isEnabled();
        }, StateType.SELENIUM_ENABLED.not(), TEAR_DOWN)).withName("Not Exists Or Not Enabled");
    }

    @Override
    public State exists() {
        return (State) State.of(() -> getFinder().findFast().isSuccess(), StateType.EXISTS, TEAR_DOWN).withName("Exists in DOM");
    }

    @Override
    public SetState displayed() {
        return (SetState) SetState.of(StateType.DISPLAYED, exists(), State.of(() -> {
            final WaitResult<WebElement> res = getFinder().findFast();
            return res.isSuccess() && res.getResult().isDisplayed();
        }, StateType.SELENIUM_DISPLAYED, TEAR_DOWN)).withName("Exists And Displayed");
    }



    @Override
    public SetState enabled() {
        return (SetState) SetState.of(StateType.ENABLED, exists(), State.of(() -> {
            final WaitResult<WebElement> res = getFinder().findFast();
            return res.isSuccess() && res.getResult().isEnabled();
        }, StateType.SELENIUM_ENABLED, TEAR_DOWN)).withName("Exists And Enabled");
    }

    @Override
    public SetState ready() {
        return (SetState) SetState.of(StateType.READY, displayed().get(), enabled().get()).withName("Ready to interact");
    }

    @Override
    public SetState clearState() {
        return (SetState) SetState.of(StateType.OTHER, exists(), State.of(() -> {
            final WaitResult<WebElement> res = getFinder().findFast();
            return res.isSuccess() && Strings.isEmpty(getBrowser().actions().getText(res.getResult()));
        }, StateType.OTHER, TEAR_DOWN)).withName("Text of the element became an empty");
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
