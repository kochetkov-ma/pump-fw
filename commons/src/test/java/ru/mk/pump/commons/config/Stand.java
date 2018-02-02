package ru.mk.pump.commons.config;


import lombok.Data;
import lombok.ToString;

@ToString(callSuper = true)
@Data
public class Stand {

    @Property(value = "enum", required = false)
    private En enumProperty;

    @Property("two")
    private Two two;

    @Property("url")
    private String url;

    @Property("count")
    private int count;

    @Property(value = "no.reporting", required = false)
    private Object testNull;

    @Property(value = "no.prop", defaultValue = "default")
    private String testDefault;

    @Property("reporting")
    private boolean reporting;

    @Property("common")
    private String common;

    @Property(value = "common.extra")
    private String extra;
}
