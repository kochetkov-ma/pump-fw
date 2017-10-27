package ru.mk.pump.web.common;

import java.io.Closeable;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("UnusedReturnValue")
public interface Activity extends Closeable {

    Map<String, Parameter> getParams();

    boolean isClosed();

    UUID getUUID();

    boolean isActive();

    Activity activate();

    Activity disable();
}
