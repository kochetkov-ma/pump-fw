package ru.mk.pump.web.browsers.builders;

import lombok.Getter;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.mk.pump.commons.exception.ConfigurationException;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.browsers.DriverBuilder;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.exceptions.BrowserException;

import java.net.MalformedURLException;
import java.net.URL;

abstract class AbstractDriverBuilder<T extends Capabilities> implements DriverBuilder {

    @Getter
    private final BrowserConfig config;
    @Getter
    private BuilderHelper builderHelper;

    //region INIT
    public AbstractDriverBuilder(BuilderHelper builderHelper, BrowserConfig config) {
        this.builderHelper = builderHelper;
        this.config = config;
    }

    public AbstractDriverBuilder(BrowserConfig config) {
        this.config = config;
        this.builderHelper = new BuilderHelper(config, new ProjectResources(getClass()));
    }
    //endregion

    @Override
    @SuppressWarnings("unchecked")
    public WebDriver createAndStartDriver() {
        if (config.isHeadless() && !isHeadlessSupport()) {
            throw new ConfigurationException("Headless mode isn't supported. Disable it in browser config");
        }
        if (config.isRemoteDriver()) {
            return createRemoteDriver(gridUrl(), (T) getSpecialCapabilities().merge(builderHelper.getCommonCapabilities()));
        } else {
            builderHelper.prepareLocalDriverPath();
            return createLocalDriver((T) getSpecialCapabilities().merge(builderHelper.getCommonCapabilities()));
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected WebDriver createRemoteDriver(URL remoteUrl, T allCapabilities) {
        return new RemoteWebDriver(remoteUrl, allCapabilities);
    }

    abstract protected WebDriver createLocalDriver(T allCapabilities);

    /**
     * @return Only special. Common auto merge further
     */
    abstract protected T getSpecialCapabilities();

    private URL gridUrl() {
        try {
            return new URL(config.getRemoteDriverUrl());
        } catch (MalformedURLException e) {
            throw new BrowserException()
                    .withTitle(Str.format("Selenium grid URL '{}' parsing error ", config.getRemoteDriverUrl()))
                    .withCause(e);
        }
    }
}
