package ru.mk.pump.web.browsers.configuration;

public enum BrowserType {
    CHROME, FIREFOX, IE, ANDROID_APP;

    public String getDriverName() {
        return name().toLowerCase();
    }

}
