package ru.mk.pump.web.elements.internal;

import java.util.Optional;
import lombok.Getter;
import org.openqa.selenium.By;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.Action;
import ru.mk.pump.web.elements.ActionExecutor;
import ru.mk.pump.web.elements.State;
import ru.mk.pump.web.elements.StateResolver;
import ru.mk.pump.web.page.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractElement implements InternalElement {

    private final By avatarBy;

    @Getter
    private final Waiter waiter;

    @Getter
    private final Browser browser;

    private final InternalElement parentElement;

    private final ActionExecutor actionExecutor;

    private final StateResolver stateResolver;

    @Getter
    private final Finder finder;

    private String elementName;

    @Getter
    private String elementDescription;

    private Page page;

    private int listIndex = -1;

    //region CONSTRUCTORS
    public AbstractElement(By avatarBy, Page page) {
        this(avatarBy, waiter, page, null);
    }

    public AbstractElement(By avatarBy ,Page page, InternalElement parentElement) {
        this.avatarBy = avatarBy;
        this.browser = page.getBrowser();
        this.page = page;
        this.parentElement = parentElement;

        this.finder = new Finder(this);
        this.stateResolver = newStateResolver();
        this.actionExecutor = newActionExecutor(stateResolver);
    }

    public AbstractElement(By avatarBy, Browser browser, Waiter waiter) {
        this(avatarBy, waiter, browser, null);
    }

    public AbstractElement(By avatarBy, Waiter waiter, Browser browser, InternalElement parentElement) {
        this.avatarBy = avatarBy;
        this.waiter = waiter;
        this.browser = browser;
        this.parentElement = parentElement;

        this.finder = new Finder(this);
        this.stateResolver = newStateResolver();
        this.actionExecutor = newActionExecutor(stateResolver);
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
    public WaitResult<Boolean> wait(State elementState) {
        return null;
    }

    @Override
    public <T> WaitResult<T> wait(Action<T> elementState) {
        return null;
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
    public String text() {
        return null;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void checkIsReady() {

    }

    @Override
    public boolean isList() {
        return listIndex != -1;
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
    public int getIndex() {
        return listIndex;
    }

    @Override
    public String getText() {
        return actionExecutor.execute(getTextAction());
    }

    @Override
    public Action getClickAction() {

        return null;
    }

    @Override
    public Action<String> getTextAction() {

        return null;
    }

    @Override
    public State exists() {
        return new State(() -> getFinder().get().isSuccess(), "Exists");
    }

    @Override
    public State displayed() {
        return new State(() -> getFinder().get().getResult(), "Exists");
    }

    @Override
    public State enabled() {
        return null;
    }
}
