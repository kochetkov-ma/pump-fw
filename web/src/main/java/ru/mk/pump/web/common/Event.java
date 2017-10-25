package ru.mk.pump.web.common;

import java.util.Map;

public interface Event {

    String getName();

    Map<String, Parameter> getParams();
}
