package ru.mk.pump.web.browsers.builders;

import com.google.common.collect.ImmutableMap;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.mk.pump.commons.constants.MainConstants;
import ru.mk.pump.commons.utils.FileUtils;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.constants.WebConstants;

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
        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder().
            put("profile.default_content_settings.popups", 0).
            put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1).
            put("download.prompt_for_download", "false");
        if (Objects.nonNull(getConfig().getDownloadDirPath())) {
            final Path path = Paths.get(MainConstants.HOME).resolve(getConfig().getDownloadDirPath());
            FileUtils.createIfNotExists(path);
            builder.put("download.default_directory", path.toString());
        }

        if (Objects.nonNull(getConfig().getBrowserBinPath()) && FileUtils.isExistsAndValid(getConfig().getBrowserBinPath())){
            chromeOptions.setBinary(getConfig().getBrowserBinPath());
        }

        chromeOptions.addArguments("no-sandbox");
        chromeOptions.addArguments("chrome.switches","--disable-extensions");

        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        chromeOptions.addArguments("-incognito");
        chromeOptions.addArguments("test-type");
        chromeOptions.addArguments("--enable-automation");
        chromeOptions.addArguments("test-type=browser");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--disable-popup-blocking");

        chromeOptions.setExperimentalOption("prefs", builder.build());
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        getBuilderHelper().findLocalBrowserPath().ifPresent(chromeOptions::setBinary);

        /*size*/
        if (getConfig().getSizeOrDevice().useSize()) {
            chromeOptions.addArguments("window-size=" + getSize(), "--no-sandbox");
        } else if (getConfig().getSizeOrDevice().isFullScreen()) {
            if (getConfig().isRemoteDriver()) {
                /*start-maximized не всегда работает на удаленном драйвере*/
                chromeOptions.addArguments("window-size=" + WebConstants.DEFAULT_FULLSCREEN, "--no-sandbox");
            } else {
                chromeOptions.addArguments("start-maximized", "--no-sandbox");
            }
        }
        /*headless*/
        if (getConfig().isHeadless()) {
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--disable-gpu");
        }
        return chromeOptions;
    }

    private String getSize() {
        return getConfig().getSizeOrDevice().getX() + "," + getConfig().getSizeOrDevice().getY();
    }

    @Override
    public boolean isHeadlessSupport() {
        return true;
    }
}