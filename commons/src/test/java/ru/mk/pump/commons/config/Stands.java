package ru.mk.pump.commons.config;

import lombok.Getter;
import lombok.ToString;

@Config("env")
@ToString
@Getter
public class Stands {

    @Property("complex")
    private boolean complex;

    @Property("stand")
    private Stand standOne;

    @Property("standA")
    private Stand standTwo;

    @Property("name")
    private String name;
}
