package ru.mk.pump.web.elements.internal;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.Action;
import ru.mk.pump.web.elements.ActionExecutor;
import ru.mk.pump.web.elements.ActionFactory;
import ru.mk.pump.web.elements.ElementWaiter;
import ru.mk.pump.web.elements.MultiState;
import ru.mk.pump.web.elements.State;
import ru.mk.pump.web.elements.State.StateType;
import ru.mk.pump.web.elements.StateResolver;
import ru.mk.pump.web.page.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractElement implements InternalElement {

    private final By avatarBy;

    @Getter
    private final ElementWaiter waiter;

    @Getter
    private final Browser browser;

    private final InternalElement parentElement;

    @Getter(AccessLevel.PROTECTED)
    private final ActionExecutor actionExecutor;

    private final StateResolver stateResolver;

    @Getter
    private final Finder finder;

    private final ActionFactory actions;

    private String elementName;

    @Getter
    private String elementDescription;

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

    public AbstractElement(By avatarBy, InternalElement parentElement, ElementWaiter waiter ) {
        this.avatarBy = avatarBy;
        this.waiter = waiter;
        this.browser = parentElement.getBrowser();
        this.parentElement = parentElement;

        this.finder = new Finder(this);
        this.stateResolver = newStateResolver();
        this.actionExecutor = newActionExecutor(stateResolver);
        this.actions = new ActionFactory(this);
    }

    public AbstractElement(By avatarBy, InternalElement parentElement, Browser browser, ElementWaiter waiter ) {
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
        return actions.newAction(() -> getFinder().get().click(), "Click");
    }

    @Override
    public Action<String> getTextAction() {
        return actions.newAction(() -> getBrowser().actions().getText(getFinder().get()), "Get text");
    }

    @Override
    public State exists() {
        return State.of(() -> getFinder().find().isSuccess(), StateType.EXISTS).withName("Exists");
    }

    @Override
    public MultiState displayed() {
        return MultiState.of(StateType.OTHER, exists(), State.of(() -> getFinder().get().isDisplayed(), StateType.MULTI_FINAL)).withName("Displayed");
    }

    @Override
    public MultiState enabled() {
        return MultiState.of(StateType.OTHER, exists(), State.of(() -> getFinder().get().isEnabled(), StateType.MULTI_FINAL)).withName("Enabled");
    }

    @Override
    public MultiState ready() {
        return MultiState.of(StateType.READY, displayed().get(), enabled().get());
    }
}
