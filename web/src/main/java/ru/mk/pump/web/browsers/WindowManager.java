package ru.mk.pump.web.browsers;

import java.util.Set;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.activity.AbstractActivityManager;
import ru.mk.pump.commons.activity.Activity;
import ru.mk.pump.commons.activity.NamedEvent;
import ru.mk.pump.web.exceptions.BrowserException;

@SuppressWarnings("unchecked")
public class WindowManager extends AbstractActivityManager {

    @Setter
    private Browser browser;

    WindowManager(@Nullable AbstractBrowser browser) {
        super();
        this.browser = browser;
        setFilterActivityClass(Window.class);
        add(browser);
    }

    public Window newTab() {
        final Set<String> windows = browser.getDriver().getWindowHandles();
        browser.actions().executeScript("window.open()");
        final Set<String> newWindows = browser.getDriver().getWindowHandles();
        newWindows.removeAll(windows);
        if (newWindows.size() == 0) {
            throw new BrowserException("Cannot create new Tab by JS");
        }

        final Window window = Window.of(this, browser.getDriver(), newWindows.iterator().next());
        addAndActivate(window);
        return window;
    }

    @Override
    public void onClose(NamedEvent namedEvent, Activity activity) {
        super.onClose(namedEvent, activity);
        if (isTargetActivity(activity)) {
            if (getActive().isPresent()) {
                getActive().get().activate();
            } else if (getPrev().isPresent()) {
                getPrev().get().activate();
            }
        }
    }

    @Override
    public void onActivate(NamedEvent namedEvent, Activity activity) {
        if (activity instanceof AbstractBrowser) {
            addAndActivate(Window.of(this, browser.getDriver(), browser.getDriver().getWindowHandle()));
        } else {
            super.onActivate(namedEvent, activity);
        }
    }
}
