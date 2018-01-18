package ru.mk.pump.web.elements.internal.interfaces;

import ru.mk.pump.web.elements.internal.SetState;
import ru.mk.pump.web.elements.internal.State;

public interface ElementState {

    SetState exists();

    State notExists();

    SetState displayed();

    SetState notDisplayed();

    SetState enabled();

    SetState notEnabled();

    SetState ready();

    SetState clearState();
}
