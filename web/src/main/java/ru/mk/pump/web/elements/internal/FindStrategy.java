package ru.mk.pump.web.elements.internal;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

abstract class FindStrategy implements StrictInfo {

    @Getter(AccessLevel.PROTECTED)
    private final InternalElement target;

    FindStrategy(InternalElement internalElement) {

        this.target = internalElement;
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoBuilder(getClass().getSimpleName())
            .put("target element", target.toString())
            .build();
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
