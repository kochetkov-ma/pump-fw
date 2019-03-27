package ru.mk.pump.web.exceptions;

import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@NoArgsConstructor
public class ElementNotFoundException extends ElementFinderException {

    public ElementNotFoundException(@Nullable String title) {
        this(title, null);
    }

    public ElementNotFoundException(@Nullable String title, @Nullable Throwable cause) {
        super(title, cause);
    }
}
