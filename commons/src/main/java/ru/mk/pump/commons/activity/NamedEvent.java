package ru.mk.pump.commons.activity;

import lombok.ToString;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;

@SuppressWarnings("unused")
@ToString
public final class NamedEvent implements Event {

    private final Parameters parameters = Parameters.of();

    private final String name;

    private NamedEvent(String name) {
        this.name = name;
    }

    public static NamedEvent of(String name) {
        return new NamedEvent(name);
    }

    public NamedEvent addParam(Parameter<?> param) {
        parameters.add(param);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Parameters getParams() {
        return parameters;
    }
}
