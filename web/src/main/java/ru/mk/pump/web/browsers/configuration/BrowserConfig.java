package ru.mk.pump.web.browsers.configuration;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.mk.pump.commons.activity.Parameter;

@RequiredArgsConstructor
@Data
public class BrowserConfig {

    private final boolean remoteDriver;

    private String remoteDriverUrl;

    private String downloadDirPath;

    private final Size sizeOrDevice;

    private final BrowserType type;

    private String browserBinPath;

    private String webDriverPath;

    private String version;

    private boolean debug = false;

    private boolean selenoidVnc = false;

    private Map<String, Parameter> extraParams = Maps.newHashMap();
}
