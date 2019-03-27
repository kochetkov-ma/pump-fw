package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;
import ru.mk.pump.web.page.api.Page;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@NoArgsConstructor
public class PageException extends WebException {

    public PageException(@Nullable String title, @Nullable Page page) {
        this(title, page, null);
    }

    public PageException(@Nullable String title, @Nullable Page page, @Nullable Throwable cause) {
        super(title, cause);
        withPage(page);
    }

    @Override
    protected PageException withPage(@Nullable Page page) {
        return (PageException) super.withPage(page);
    }
}
