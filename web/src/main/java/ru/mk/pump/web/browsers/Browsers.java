package ru.mk.pump.web.browsers;

import java.util.UUID;
import ru.mk.pump.web.browsers.builders.ChromeDriverBuilder;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;

@SuppressWarnings("WeakerAccess")
public class Browsers {

    private static DriverBuilder getBuilder(BrowserConfig browserConfig) {
        switch (browserConfig.getType()) {
            case CHROME:
                return new ChromeDriverBuilder(browserConfig);
            case FIREFOX:
                throw new UnsupportedOperationException();
            case IE:
                throw new UnsupportedOperationException();
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Browser newBrowser(BrowserConfig browserConfig) {
        return new AbstractBrowser(getBuilder(browserConfig), UUID.randomUUID()) {
        };
    }
}
