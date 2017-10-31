package ru.mk.pump.web.browsers.builders;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;

public class BuilderHelper {

    private final BrowserConfig browserConfig;

    public BuilderHelper(BrowserConfig browserConfig) {
        this.browserConfig = browserConfig;
    }

    public void prepareLocalDriverPath() {
        if (!Strings.isEmpty(browserConfig.getWebDriverPath())) {
            if (Objects.nonNull(browserConfig.getWebDriverPath()) && Files.exists(Paths.get(browserConfig.getWebDriverPath()))) {
                System.setProperty("webdriver." + browserConfig.getType().getDriverName() + ".driver", browserConfig.getWebDriverPath());
            }
        }
    }

    public Optional<String> findLocalBrowserPath() {
        return Optional.empty();
    }

    public Capabilities getCommonCapabilities() {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        if (browserConfig.isDebug()) {
            capabilities.setCapability("enableVNC", true); /*selenoid*/
        }
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);

        capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");
        capabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS, false);

        if (!Strings.isEmpty(browserConfig.getVersion())) {
            capabilities.setVersion(browserConfig.getVersion());
        }
        return capabilities;
    }

}
