package ru.mk.pump.web.elements.internal.interfaces;

import ru.mk.pump.web.elements.internal.SetState;
import ru.mk.pump.web.elements.internal.State;

public interface ElementState {

    State exists();

    SetState displayed();

    SetState enabled();

    SetState ready();

    SetState clear();
}
