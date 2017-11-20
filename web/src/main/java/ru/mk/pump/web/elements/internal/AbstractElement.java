package ru.mk.pump.web.elements.internal;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.internal.State.StateType;
import ru.mk.pump.web.page.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractElement implements InternalElement {

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

    private final ActionFactory actions;

    private final Consumer<WaitResult<Boolean>> TEAR_DOWN = stateWaitResult -> getFinder().getLast()
        .ifPresent(waitResult -> stateWaitResult.withCause(waitResult.getCause()));

    private String elementName = "empty (strongly recommend to add)";

    @Getter
    private String elementDescription = "empty (recommend to add)";

    private Page page;

    private int listIndex = -1;

    //region CONSTRUCTORS
    public AbstractElement(By avatarBy, Page page, ElementWaiter waiter) {
        this(avatarBy, null, page, waiter);
    }

    public AbstractElement(By avatarBy, InternalElement parentElement, Page page, ElementWaiter waiter) {
        this.avatarBy = avatarBy;
        this.browser = page.getBrowser();
        this.page = page;
        this.waiter = waiter;
        this.parentElement = parentElement;

        this.finder = new Finder(this);
        this.stateResolver = newStateResolver();
        this.actionExecutor = newActionExecutor(stateResolver);
        this.actions = new ActionFactory(this);
    }

    public AbstractElement(By avatarBy, Browser browser, ElementWaiter waiter) {
        this(avatarBy, null, browser, waiter);
    }

    public AbstractElement(By avatarBy, InternalElement parentElement, ElementWaiter waiter) {
        this.avatarBy = avatarBy;
        this.waiter = waiter;
        this.browser = parentElement.getBrowser();
        this.parentElement = parentElement;

        this.finder = new Finder(this);
        this.stateResolver = newStateResolver();
        this.actionExecutor = newActionExecutor(stateResolver);
        this.actions = new ActionFactory(this);
    }

    public AbstractElement(By avatarBy, InternalElement parentElement, Browser browser, ElementWaiter waiter) {
        this.avatarBy = avatarBy;
        this.waiter = waiter;
        this.browser = browser;
        this.parentElement = parentElement;

        this.finder = new Finder(this);
        this.stateResolver = newStateResolver();
        this.actionExecutor = newActionExecutor(stateResolver);
        this.actions = new ActionFactory(this);
    }
    //endregion

    protected StateResolver newStateResolver() {
        return new StateResolver(this);
    }

    protected ActionExecutor newActionExecutor(StateResolver stateResolver) {
        return new ActionExecutor().withStateResolver(stateResolver);
    }

    public AbstractElement withName(String elementName) {
        this.elementName = elementName;
        return this;
    }

    public AbstractElement withDescription(String elementDescription) {
        this.elementDescription = elementDescription;
        return this;
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
    public Optional<Page> getPage() {
        return Optional.ofNullable(page);
    }

    @Override
    public Optional<InternalElement> getParent() {
        return Optional.ofNullable(parentElement);
    }

    @Override
    public ElementWaiter getWaiter() {
        return waiter.newInstance();
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
        return actions.newAction(WebElement::click, "Click");
    }

    @Override
    public Action<String> getTextAction() {
        return actions.newAction(webElement -> {
            return getBrowser().actions().getText(webElement);
        }, "Get text");
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
}
