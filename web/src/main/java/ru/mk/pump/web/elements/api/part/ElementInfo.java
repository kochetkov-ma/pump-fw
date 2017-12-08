package ru.mk.pump.web.elements.api.part;

import org.openqa.selenium.By;

public interface ElementInfo {

    /**
     * Optional, but important for business logic
     * @return element text name
     */
    String getName();

    /**
     * Optional
     * @return element text description
     */
    String getDescription();

    /**
     * @return element locator
     */
    By getBy();

    /**
     * @return <b>true</b> - this element is one of elements list with the same locator / <b>false</b> - single element
     */
    boolean isList();

    /**
     * @return '<b>-1</b>' if {@link #isList()} = false / '<b>>0</b>' if {@link #isList()} = true
     */
    int getIndex();
}
