package ru.mk.pump.commons.config;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class Stand {

    @Getter
    @Property("url")
    private String url;

    @Getter
    @Property("count")
    private int count;

    @Getter
    @Property(value = "no.reporting", required = false)
    private Object testNull;

    @Getter
    @Property(value = "no.prop", defaultValue = "default")
    private String testDefault;

    @Getter
    @Property("reporting")
    private boolean reporting;

    @Getter
    @Property("common")
    private String common;

    @Getter
    @Property(value = "common.extra")
    private String extra;
}
