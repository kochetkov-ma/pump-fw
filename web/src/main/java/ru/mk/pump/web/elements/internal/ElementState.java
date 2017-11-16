package ru.mk.pump.web.elements.internal;

import java.util.List;
import ru.mk.pump.web.elements.State;

public interface ElementState {

    State exists();

    List<State> displayed();

    List<State> enabled();

    List<State> ready();
}
