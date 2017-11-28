package ru.mk.pump.web.elements.internal.interfaces;

import java.util.List;
import org.openqa.selenium.By;

public interface ElementAction {

    Action getClickAction();

    Action getTextAction();

    Action getClearAction();

    Action getFocusAction();

    Action getInputAction(CharSequence ... keys);

}
