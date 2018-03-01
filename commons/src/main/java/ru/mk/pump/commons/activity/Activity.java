package ru.mk.pump.commons.activity;

import ru.mk.pump.commons.helpers.Parameters;

import java.io.Closeable;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface Activity extends Closeable {

    Parameters getParams();

    boolean isClosed();

    String getUUID();

    boolean isActive();

    Activity activate();

    Activity disable();
}
