package ru.mk.pump.web.browsers;

import lombok.NonNull;
import lombok.Setter;
import org.openqa.selenium.Dimension;
import ru.mk.pump.commons.activity.AbstractActivityManager;
import ru.mk.pump.commons.activity.Activity;
import ru.mk.pump.commons.activity.NamedEvent;
import ru.mk.pump.web.exceptions.BrowserException;

import java.util.Set;

@SuppressWarnings("unchecked")
public class WindowManager extends AbstractActivityManager {

    @Setter
    private Browser browser;

    @SuppressWarnings("WeakerAccess")
    protected WindowManager(@NonNull AbstractBrowser browser) {
        super();
        this.browser = browser;
        setFilterActivityClass(Window.class);
        add(browser);

    }

    public WindowManager setSize(Dimension dimension) {
        browser.getDriver().manage().window().setSize(dimension);
        return this;
    }

    public WindowManager mazimize() {
        browser.getDriver().manage().window().maximize();
        return this;
    }

    public Window newTab() {
        final Set<String> windows = browser.getDriver().getWindowHandles();
        /*new tab through JS*/
        browser.actions().executeScript("window.open()");
        /*check new tab*/
        final Set<String> newWindows = browser.getDriver().getWindowHandles();
        newWindows.removeAll(windows);
        if (newWindows.size() == 0) {
            throw new BrowserException("Cannot create new Tab by JS");
        }
        /*register new logical activity*/
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
