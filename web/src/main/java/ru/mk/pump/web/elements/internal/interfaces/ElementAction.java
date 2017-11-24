package ru.mk.pump.web.elements.internal.interfaces;

import java.util.List;
import org.openqa.selenium.By;

public interface ElementAction {

    Action getClickAction();

    Action getTextAction();

    Action getFocusAction();

    <T extends InternalElement> Action<List<T>> getSubItemsAction(By by, Class<T> elementClass);

    Action getInputAction();

    <T extends InternalElement> Action getSubItemAction(By by, Class<T> elementClass);
}
