package ru.mk.pump.web.elements.enums;

import ru.mk.pump.web.constants.ElementParams;

/**
 * Element Parameter enum {@link ElementParams}
 * For using as Element Parameter do this {@link ElementParams#enumAsParam(Enum)}
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
     * - if not empty then KEYBOARD
     *
     * Details in the type description {@link ClearType}
     */
    ADVANCED

}
