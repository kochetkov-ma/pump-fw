package ru.mk.pump.web.elements.internal;

import ru.mk.pump.web.elements.State;

public interface ElementState {

    State exists();

    State displayed();

    State enabled();
}
