package ru.mk.pump.web.elements.enums;

import ru.mk.pump.web.constants.ElementParams;

/**
 * PElement Parameter enum {@link ElementParams}
 * For using as PElement Parameter do this {@link ElementParams#enumAsParam(Enum)}
 */
public enum ClearType {

    /**
     * WebDriver
     *
     * Details in the type description {@link ClearType}
     */
    BASIC,
    /**
     * With keyboard emulation Ctrl+A and delete
     *
     * Details in the type description {@link ClearType}
     */
    KEYBOARD,
    /**
     * - BASIC
     * - check element text
     * - if not nonArg then KEYBOARD
     *
     * Details in the type description {@link ClearType}
     */
    ADVANCED

}
