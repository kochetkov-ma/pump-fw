package ru.mk.pump.web.browsers;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BrowserConfig {

    private final BrowserType type;

    public BrowserType getType() {
        return type;
    }

}
