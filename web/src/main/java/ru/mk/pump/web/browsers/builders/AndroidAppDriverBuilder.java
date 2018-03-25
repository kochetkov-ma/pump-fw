package ru.mk.pump.web.browsers.builders;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;

import java.net.URL;

public class AndroidAppDriverBuilder extends AbstractDriverBuilder<DesiredCapabilities> {

    public AndroidAppDriverBuilder(BrowserConfig browserConfig) {
        super(browserConfig, new BuilderHelper(browserConfig));
    }

    @Override
    protected WebDriver createLocalDriver(DesiredCapabilities caps) {
        throw new IllegalStateException("Cannot create local android driver. You must enable browser.remote and define browser.remote.url");
    }

    @Override
    protected WebDriver createRemoteDriver(URL remoteUrl, DesiredCapabilities allCapabilities) {
        return new AndroidDriver<MobileElement>(remoteUrl, allCapabilities);
    }

    @Override
    protected DesiredCapabilities getSpecialCapabilities() {
        return getAndroidAppCapabilities();
    }

    private DesiredCapabilities getAndroidAppCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", getConfig().getAppiumPlatformVersion());
        capabilities.setCapability("deviceName", getConfig().getAppiumDeviceName());
        if (!Strings.isEmpty(getConfig().getAppiumAppActivity())) {
            capabilities.setCapability("appActivity", getConfig().getAppiumAppActivity());
        }
        if (!Strings.isEmpty(getConfig().getAppiumAppPackage())) {
            capabilities.setCapability("appPackage", getConfig().getAppiumAppPackage());
        }
        if (!Strings.isEmpty(getConfig().getAppiumApp())) {
            capabilities.setCapability("app", getConfig().getAppiumApp());
        }
        return capabilities;
    }

    private String getSize() {
        return getConfig().getSizeOrDevice().getX() + "," + getConfig().getSizeOrDevice().getY();
    }

}
