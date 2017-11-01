package ru.mk.pump.commons.config;

import lombok.Getter;
import lombok.ToString;

@Config("env")
@ToString
@Getter
public class Stands {

    @Property("complex")
    private boolean complex;

    @Config("stand")
    private Stand standOne;

    @Config("standA")
    private Stand standTwo;

    @Property("name")
    private String name;
}
