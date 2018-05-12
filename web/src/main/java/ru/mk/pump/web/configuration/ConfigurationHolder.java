package ru.mk.pump.web.configuration;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.config.ConfigurationHelper;
import ru.mk.pump.commons.config.ConfigurationsLoader;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;

@SuppressWarnings({"WeakerAccess", "unused"})
@Slf4j
public class ConfigurationHolder {

    public static final String DEFAULT_SYSTEM_ENV_NAME = "pump.configuration.path";

    public static final String DEFAULT_CLASSPATH_RESOURCE = "pump.properties";

    private static final Object lock = new Object();

    private static final ReentrantLock rLock = new ReentrantLock();

    private static volatile ConfigurationHolder INSTANCE;

    @Getter
    private final ConfigurationHelper<Configuration> configurationHelper;


    private ConfigurationHolder(Configuration configuration) {
        this.configurationHelper = new ConfigurationHelper<>(DEFAULT_SYSTEM_ENV_NAME, DEFAULT_CLASSPATH_RESOURCE, configuration);
    }

    public static void cleanup() {
        INSTANCE = null;
    }

    public static ConfigurationHolder instance() {
        ConfigurationHolder localInstance = INSTANCE;
        if (localInstance == null) {
            synchronized (lock) {
                localInstance = INSTANCE;
                if (localInstance == null) {
                    INSTANCE = localInstance = new ConfigurationHolder(getDefaultConfiguration());
                }
            }
        }
        return localInstance;
    }

    public static Configuration get() {

        Configuration localInstance = instance().configurationHelper.config();
        if (localInstance != null) {
            return localInstance;
        }
        if (rLock.tryLock()) {
            rLock.lock();
            instance().configurationHelper.loadAuto();
            rLock.unlock();
        }
        return instance().configurationHelper.config();
    }

    //region INIT
    public static void init(ConfigurationsLoader configurationsLoader) {
        instance().configurationHelper.loadFromLoader(configurationsLoader);
    }

    public static void init(@NonNull InputStream inputStream) {
        instance().configurationHelper.loadFromStream(inputStream);
    }

    public static void init(@NonNull String resourcePath) {
        instance().configurationHelper.loadFromResource(resourcePath);
    }

    public static void init(@NonNull Path path) {
        instance().configurationHelper.loadFromFile(path);
    }

    public static void initSystemEnv(String envVarWithResourceNameInClassPath) {
        instance().configurationHelper.loadFromSystemEnv(envVarWithResourceNameInClassPath);
    }

    public static void initDefault() {
        instance().configurationHelper.loadAuto();
    }
    //endregion

    private static Configuration getDefaultConfiguration() {
        Configuration config = new Configuration();
        config.setBrowserConfig(BrowserConfig.of(BrowserType.CHROME));
        config.setApplicationName("undefined");
        config.getElement().setWindowWidthOffset(-1);
        return config;
    }
}