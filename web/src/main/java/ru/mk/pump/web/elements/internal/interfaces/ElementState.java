package ru.mk.pump.web.elements.internal.interfaces;

import ru.mk.pump.web.elements.internal.SetState;

public interface ElementState {

    SetState exists();

    SetState displayed();

    SetState enabled();

    SetState ready();

    SetState clearState();
}
