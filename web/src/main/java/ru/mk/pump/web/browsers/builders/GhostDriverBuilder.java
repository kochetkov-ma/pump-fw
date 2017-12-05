package ru.mk.pump.web.browsers.builders;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;

public class GhostDriverBuilder extends AbstractDriverBuilder<DesiredCapabilities> {

    public GhostDriverBuilder(BrowserConfig browserConfig) {
        super(browserConfig, new BuilderHelper(browserConfig));
    }

    @Override
    protected DesiredCapabilities getSpecialCapabilities() {
        return getCapabilities();
    }

    @Override
    protected WebDriver createLocalDriver(DesiredCapabilities allCapabilities) {
        return new PhantomJSDriver(allCapabilities);
    }

    private DesiredCapabilities getCapabilities() {
        final DesiredCapabilities options = new DesiredCapabilities();
        if (SystemUtils.IS_OS_WINDOWS) {
            options.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, ProjectResources.findResource("phantomjs.exe").toString());
        }
        //options.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{"--ignore-ssl-errors=yes"});
        return options;
    }
}
