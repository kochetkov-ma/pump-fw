package ru.mk.pump.web.elements.internal;

import ru.mk.pump.web.elements.MultiState;
import ru.mk.pump.web.elements.State;

public interface ElementState {

    State exists();

    MultiState displayed();

    MultiState enabled();

    MultiState ready();
}
