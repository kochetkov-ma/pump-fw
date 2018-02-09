package ru.mk.pump.commons.activity;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.ToString;

@SuppressWarnings("unused")
@ToString
public final class NamedEvent implements Event {

    private final Map<String, Parameter<?>> parameterMap = Maps.newHashMap();

    private final String name;

    private NamedEvent(String name) {
        this.name = name;
    }

    public static NamedEvent of(String name) {
        return new NamedEvent(name);
    }

    public NamedEvent addParam(String name, Parameter<?> param) {
        parameterMap.put(name, param);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Parameter<?>> getParams() {
        return parameterMap;
    }
}
