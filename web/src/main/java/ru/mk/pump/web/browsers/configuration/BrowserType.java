package ru.mk.pump.web.browsers.configuration;

public enum BrowserType {
    CHROME, FIREFOX, IE, PHANTOMJS, ANDROID_APP;

    public String getDriverName() {
        return name().toLowerCase();
    }

}
