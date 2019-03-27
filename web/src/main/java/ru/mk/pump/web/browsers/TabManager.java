package ru.mk.pump.web.browsers;

import com.google.common.collect.Queues;
import lombok.NonNull;
import lombok.val;
import ru.mk.pump.web.exceptions.BrowserException;

import java.util.Queue;
import java.util.Set;

@SuppressWarnings({"unchecked", "WeakerAccess"})
public class TabManager {

    private final AbstractBrowser browser;
    private final Queue<String> tabs = Queues.newArrayDeque();

    @SuppressWarnings("WeakerAccess")
    protected TabManager(@NonNull AbstractBrowser browser) {

        this.browser = browser;
    }

    public Queue<String> refresh() {

        tabs.addAll(browser.getDriver().getWindowHandles());
        return tabs;
    }

    public String closeTab() {

        refresh();
        val closingTab = tabs.poll();
        // notify
        browser.notifyOnCloseTab(browser, closingTab);
        // close tab through the driver
        browser.getDriver().close();
        // close browser if the tab was the only
        if (refresh().isEmpty()) {
            browser.close();
        }
        return closingTab;
    }

    public String openTab() {

        refresh();
        // new tab through JS
        browser.actions().executeScript("tab.open()");
        // check new tab
        final Set<String> newWindows = browser.getDriver().getWindowHandles();
        newWindows.removeAll(tabs);
        if (newWindows.size() == 0) {
            throw new BrowserException("Cannot create new Tab by JS", browser);
        }
        // register new logical activity
        final String newTab = newWindows.iterator().next();
        // notify
        browser.notifyOnOpenTab(browser, newTab);
        // switch to tab
        browser.getDriver().switchTo().window(newTab);
        tabs.add(newTab);
        return newTab;
    }
}