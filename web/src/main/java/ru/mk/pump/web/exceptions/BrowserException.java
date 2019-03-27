package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;
import ru.mk.pump.web.browsers.api.Browser;

import javax.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor
public class BrowserException extends WebException {

    public BrowserException(@Nullable String title, @Nullable Browser browser) {
        this(title, browser, null);
    }

    public BrowserException(@Nullable String title, @Nullable Browser browser, @Nullable Throwable cause) {
        super(title, cause);
        withBrowser(browser);
    }

    @Override
    public BrowserException withBrowser(@Nullable Browser browser) {
        return (BrowserException)super.withBrowser(browser);
    }
}