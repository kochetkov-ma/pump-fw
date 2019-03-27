package ru.mk.pump.web.exceptions;

import jdk.internal.jline.internal.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SubElementsNotFoundException extends ElementException {

    private static final String TITLE = "Cannot find expected sub rules by conditions : '%s'";

    public SubElementsNotFoundException(@Nullable String conditions) {
        this(conditions, null);
    }

    public SubElementsNotFoundException(@Nullable String conditions, @Nullable Throwable cause) {
        super();
        withTitle(String.format(TITLE, conditions != null ? conditions : "null"));
        withCause(cause);
    }
}
