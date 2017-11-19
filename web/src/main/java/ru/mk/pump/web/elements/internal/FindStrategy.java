package ru.mk.pump.web.elements.internal;

import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.WebElement;

public abstract class FindStrategy {

    @Getter(AccessLevel.PROTECTED)
    private final InternalElement target;


    public FindStrategy(InternalElement internalElement) {

        this.target = internalElement;
    }

    abstract public WebElement findSelf();


    protected boolean isRoot() {
        return !target.getParent().isPresent();
    }

    @Override
    public String toString() {
        return "FindStrategy(" +
                "type=" + getClass().getSimpleName() +
                ", target=" + target +
                ')';
    }
}
