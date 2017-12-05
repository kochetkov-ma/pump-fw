package ru.mk.pump.web.browsers.configuration;

public enum BrowserType {
    CHROME, FIREFOX, IE, PHANTOMJS;

    public String getDriverName() {
        return name().toLowerCase();
    }

}
