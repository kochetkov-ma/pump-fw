package ru.mk.pump.web.elements.api.concrete;

import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.Editable;

public interface CheckBox extends Element, Editable{

    void setState(State state);

    void getState(State state);

    public enum State {
        CHECKED, UNCHECED
    }

}
