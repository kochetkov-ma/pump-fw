package ru.mk.pump.web.browsers.configuration;

import lombok.Getter;

public enum BrowserType {
    CHROME("chrome"), FIREFOX("firefox"), IE("ie");

    @Getter
    private final String name;

    BrowserType(String name) {

        this.name = name;
    }
}
