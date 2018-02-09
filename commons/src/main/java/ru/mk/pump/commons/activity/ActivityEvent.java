package ru.mk.pump.commons.activity;

import org.jetbrains.annotations.Nullable;

public enum ActivityEvent {
    CLOSE(NamedEvent.of("closed")),
    DISABLE(NamedEvent.of("disabled")),
    ACTIVATE(NamedEvent.of("activated"));

    private NamedEvent namedEvent;

    ActivityEvent(NamedEvent namedEvent) {
        this.namedEvent = namedEvent;
    }

    public NamedEvent event() {
        return namedEvent;
    }

    public boolean check(@Nullable NamedEvent namedEvent) {
        return namedEvent != null && event().getName().equals(namedEvent.getName());
    }
}