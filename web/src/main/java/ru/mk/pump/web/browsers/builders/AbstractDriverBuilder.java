package ru.mk.pump.web.browsers.builders;

import java.net.MalformedURLException;
import java.net.URL;
import lombok.Getter;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.mk.pump.web.browsers.DriverBuilder;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.exceptions.BrowserException;


abstract class AbstractDriverBuilder<T extends Capabilities> implements DriverBuilder {

    @Getter
    private final BuilderHelper builderHelper;

    @Getter
    private final BrowserConfig config;

    //region INIT
    public AbstractDriverBuilder(BrowserConfig config, BuilderHelper builderHelper) {
        this.config = config;
        this.builderHelper = builderHelper;
    }
    //endregion

    @Override
    @SuppressWarnings("unchecked")
    public WebDriver createAndStartDriver() {
        if (config.isRemoteDriver()) {
            return new RemoteWebDriver(gridUrl(), getSpecialCapabilities().merge(builderHelper.getCommonCapabilities()));
        } else {
            builderHelper.prepareLocalDriverPath();
            return createLocalDriver((T) getSpecialCapabilities().merge(builderHelper.getCommonCapabilities()));
        }
    }

    abstract protected WebDriver createLocalDriver(T allCapabilities);

    /**
     *
     * @return Only special. Common auto merge further
     */
    abstract protected T getSpecialCapabilities();

    private URL gridUrl() {
        try {
            return new URL(config.getRemoteDriverUrl());
        } catch (MalformedURLException e) {
            throw new BrowserException("Selenium grid URL parsing error " + config.getRemoteDriverUrl(), e);
        }
    }
}
