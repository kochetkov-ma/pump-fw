package ru.mk.pump.web.browsers.builders;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.constants.WebConstants;

public class ChromeDriverBuilder extends AbstractDriverBuilder<ChromeOptions> {

    public ChromeDriverBuilder(BrowserConfig browserConfig) {
        super(browserConfig, new BuilderHelper(browserConfig));
    }

    @Override
    protected WebDriver createLocalDriver(ChromeOptions chromeOptions) {
        return new ChromeDriver(chromeOptions);
    }

    @Override
    protected ChromeOptions getSpecialCapabilities() {
        return getChromeOptions();
    }

    private ChromeOptions getChromeOptions() {
        final ChromeOptions chromeOptions = new ChromeOptions();
        final Map<String, Object> prefs = ImmutableMap.<String, Object>builder().
            put("profile.default_content_settings.popups", 0).
            put("download.default_directory", getConfig().getDownloadDirPath()).
            put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1).
            put("download.prompt_for_download", "false").
            build();

        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        getBuilderHelper().findLocalBrowserPath().ifPresent(chromeOptions::setBinary);

        /*set browser init size*/
        if (getConfig().getSizeOrDevice().useSize()) {
            chromeOptions.addArguments("--window-size=" + getSize() + "--no-sandbox");
        } else if (getConfig().getSizeOrDevice().isFullScreen()) {
            if (getConfig().isRemoteDriver()) {
                chromeOptions.addArguments("--window-size=" + WebConstants.DEFAULT_FULLSCREEN);
            } else {
                chromeOptions.addArguments("--start-maximized");
            }
        }

        return chromeOptions;
    }

    private String getSize() {
        return getConfig().getSizeOrDevice().getX() + "," + getConfig().getSizeOrDevice().getY();
    }

}
