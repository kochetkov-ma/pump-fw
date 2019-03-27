package ru.mk.pump.web.browsers.builders;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.mk.pump.commons.utils.FileUtils;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.utils.CapabilitiesUtils;

@SuppressWarnings("WeakerAccess")
@AllArgsConstructor
public class BuilderHelper {

    private final BrowserConfig browserConfig;
    private final ProjectResources projectResources;

    public void prepareLocalDriverPath() {
        final String path = getPath();
        if (!Str.isEmpty(path)) {
            System.setProperty("webdriver." + browserConfig.getType().getDriverName() + ".driver", path);
        }
    }

    private String getPath() {
        if (FileUtils.isExistsAndValid(browserConfig.getWebDriverPath())) {
            return browserConfig.getWebDriverPath();
        } else {
            final List<Path> pathList = projectResources.findResourceList(browserConfig.getType().getDriverName());
            if (!pathList.isEmpty()) {
                return pathList.get(0).toString();
            } else {
                return Str.empty();
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

        if (!Str.isEmpty(browserConfig.getVersion())) {
            capabilities.setVersion(browserConfig.getVersion());
        }
        if (browserConfig.getCapabilitiesFile() != null) {
            capabilities.merge(CapabilitiesUtils.loadFromProperties(browserConfig.getCapabilitiesFile(), projectResources));
        }
        return capabilities;
    }
}