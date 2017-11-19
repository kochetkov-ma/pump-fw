package ru.mk.pump.web.elements.internal;

public interface ElementState {

    State exists();

    SetState displayed();

    SetState enabled();

    SetState ready();
}
