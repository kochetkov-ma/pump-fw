package ru.mk.pump.web.browsers.configuration;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.mk.pump.commons.config.Property;
import ru.mk.pump.commons.helpers.Parameters;

@RequiredArgsConstructor
@Data
public class BrowserConfig {

    public static BrowserConfig of(BrowserType type) {
        return of(null, type);
    }

    public static BrowserConfig of(Size sizeOrDevice, BrowserType type) {
        BrowserConfig res = new BrowserConfig();
        res.setType(type);
        res.setSizeOrDevice(sizeOrDevice);
        return res;
    }

    @Property(value = "remote", defaultValue = "false")
    private boolean remoteDriver;

    @Property(value = "remote.url", required = false)
    private String remoteDriverUrl;

    @Property(value = "download.dir", defaultValue = "auto_downloads")
    private String downloadDirPath = "auto_downloads";

    @Property(value = "size", required = false)
    private Size sizeOrDevice = Size.of(true);

    @Property("type")
    private BrowserType type;

    @Property(value = "bin.path", required = false)
    private String browserBinPath;

    @Property(value = "driver.path", required = false)
    private String webDriverPath;

    @Property(value = "version", required = false)
    private String version;

    @Property(value = "debug", defaultValue = "false")
    private boolean debug = false;

    @Property(value = "vnc", defaultValue = "false")
    private boolean selenoidVnc = false;

    @Property(value = "capabilities.file", defaultValue = "false")
    private String capabilitiesFile;

    private Parameters extraParams = Parameters.of();

    private DesiredCapabilities capabilities;

    /*APPIUM*/
    @Property(value = "appium.platform.name", required = false)
    private String appiumPlatformName;

    @Property(value = "appium.platform.version", required = false)
    private String appiumPlatformVersion;

    @Property(value = "appium.device.name", required = false)
    private String appiumDeviceName;

    @Property(value = "appium.app.path", required = false)
    private String appiumApp;

    @Property(value = "appium.app.package", required = false)
    private String appiumAppPackage;

    @Property(value = "appium.app.activity", required = false)
    private String appiumAppActivity;

}
