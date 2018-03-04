package ru.mk.pump.web.browsers;

import ru.mk.pump.commons.constants.MainConstants;
import ru.mk.pump.commons.utils.FileUtils;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("WeakerAccess")
public class DownloadManager {

    private BrowserConfig browserConfig;
    private Path downloadDir;

    public DownloadManager(BrowserConfig browserConfig) {
        this.browserConfig = browserConfig;
    }

    public Path getDownloadDir() {
        if (downloadDir == null) {
            downloadDir = Paths.get(MainConstants.HOME).resolve(browserConfig.getDownloadDirPath());
        }
        FileUtils.createIfNotExists(downloadDir);
        return downloadDir;

    }

}
