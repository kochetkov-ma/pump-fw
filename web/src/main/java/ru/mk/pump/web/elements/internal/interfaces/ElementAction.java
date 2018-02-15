package ru.mk.pump.web.elements.internal.interfaces;

public interface ElementAction {

    Action getClickAction();

    Action getTextAction();

    Action getClearAction();

    Action getFocusAction();

    Action getInputAction(CharSequence ... keys);

}
