package ru.mk.pump.commons.activity;

import java.util.Map;

public interface Event {

    String getName();

    Map<String, Parameter> getParams();
}
