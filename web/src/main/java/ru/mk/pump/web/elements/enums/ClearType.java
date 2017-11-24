package ru.mk.pump.web.elements.enums;

public enum ClearType {

    /**
     * WebDriver
     */
    BASIC,
    /**
     * With keyboard emulation Ctrl+A and delete
     */
    KEYBOARD,
    /**
     * - BASIC
     * - check element text
     * - if not empty then KEYBOARD
     */
    ADVANCED

}
