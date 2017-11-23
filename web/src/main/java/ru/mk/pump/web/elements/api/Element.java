package ru.mk.pump.web.elements.api;

import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;

public interface Element extends PrettyPrinter, StrictInfo{

    String getText();

    void click();

    boolean isDisplayed();

    boolean isNotDisplayed();

}
