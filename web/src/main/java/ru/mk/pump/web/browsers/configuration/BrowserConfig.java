package ru.mk.pump.web.browsers.configuration;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.mk.pump.commons.helpers.Parameter;
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

    @Property(value = "download.dir", required = false)
    private String downloadDirPath;

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

    private Parameters extraParams = Parameters.of();
}
