package ru.mk.pump.web.elements.internal.interfaces;

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
     * @return full By with parents as String
     */
    String fullByAsString();
}
