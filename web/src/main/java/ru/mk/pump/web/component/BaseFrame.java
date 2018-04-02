package ru.mk.pump.web.component;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.internal.FindFrameStrategy;
import ru.mk.pump.web.elements.internal.Finder;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
public class BaseFrame extends BaseComponent {

    @Setter
    @Getter
    private boolean active;

    public BaseFrame(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public BaseFrame(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public BaseFrame(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    protected Finder newDelegateFinder() {
        return super.newDelegateFinder().setFindStrategy(new FindFrameStrategy(this));
    }
}