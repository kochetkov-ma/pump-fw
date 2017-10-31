package ru.mk.pump.web.browsers.configuration;

public enum BrowserType {
    CHROME, FIREFOX, IE;

    public String getDriverName() {
        return name().toLowerCase();
    }

}
