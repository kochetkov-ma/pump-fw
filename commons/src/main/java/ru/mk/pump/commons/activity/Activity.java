package ru.mk.pump.commons.activity;

import java.io.Closeable;
import java.util.Map;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface Activity extends Closeable {

    Map<String, Parameter<?>> getParams();

    boolean isClosed();

    String getUUID();

    boolean isActive();

    Activity activate();

    Activity disable();
}
