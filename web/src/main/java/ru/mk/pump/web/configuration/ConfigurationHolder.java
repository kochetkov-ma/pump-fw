package ru.mk.pump.web.configuration;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.MoreObjects;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.config.ConfigurationsLoader;
import ru.mk.pump.commons.utils.EnvVariables;
import ru.mk.pump.commons.utils.FileUtils;
import ru.mk.pump.commons.utils.Preconditions;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.exceptions.ConfigurationException;

@SuppressWarnings({"WeakerAccess", "unused"})
@Slf4j
public class ConfigurationHolder {

    public static final String DEFAULT_SYSTEM_ENV_NAME = "pump.configuration.path";

    public static final String DEFAULT_CLASSPATH_RESOURCE = "pump.properties";

    private static final Object lock = new Object();

    private static volatile ConfigurationHolder INSTANCE;

    @Getter
    private static volatile ConfigurationsLoader loader;

    private final Configuration configuration;


    private ConfigurationHolder(Configuration configuration) {
        this.configuration = configuration;
    }

    static void cleanup() {
        INSTANCE = null;
        loader = null;
    }

    public static Configuration get() {
        //noinspection UnusedAssignment
        ConfigurationHolder r = INSTANCE;
        if (INSTANCE == null) {
            synchronized (lock) {
                r = INSTANCE;
                if (r == null) {
                    initDefault();
                }
            }
        }
        return INSTANCE.configuration;
    }

    //region INIT
    public static void init(Configuration configurationObject) {
        INSTANCE = new ConfigurationHolder(configurationObject);
        log.info("[PUMP-CONFIG] Loading was COMPLETED");
    }

    public static void init(ConfigurationsLoader configurationsLoader) {
        loader = configurationsLoader;
        loader.load();
        init(loader.toObject(Configuration.class));
    }

    public static void init(@NonNull InputStream inputStream) {
        log.info("[PUMP-CONFIG] Try to load from configuration InputStream");
        init(new ConfigurationsLoader(inputStream));
    }

    public static void init(@NonNull String resourcePath) {
        log.info("[PUMP-CONFIG] Try to load classpath configuration from '{}'", resourcePath);
        if (FileUtils.isExistsAndValid(resourcePath)) {
            //noinspection ConstantConditions
            init(Paths.get(resourcePath));
        } else {
            init(getResource(resourcePath));
        }
    }

    public static void init(@NonNull Path path) {
        log.info("[PUMP-CONFIG] Try to load file system path configuration from '{}'", path);
        if (Files.notExists(path)) {
            throwException("path", path.toString(), "file system");
        }
        init(new ConfigurationsLoader(path));
    }

    public static void initSystemEnv(String envVarWithResourceNameInClassPath) {
        log.info("[PUMP-CONFIG] Try to load environment variables configuration from '{}'", envVarWithResourceNameInClassPath);
        Preconditions.checkStringNotBlank(envVarWithResourceNameInClassPath);
        if (EnvVariables.has(envVarWithResourceNameInClassPath)) {
            //noinspection ConstantConditions
            init(EnvVariables.get(envVarWithResourceNameInClassPath));
        } else {
            throwException("name", envVarWithResourceNameInClassPath, "environment variables");
        }

    }

    public static void initDefault() {
        log.info("[PUMP-CONFIG] Try to load default configuration");
        if (EnvVariables.has(DEFAULT_SYSTEM_ENV_NAME)) {
            try {
                initSystemEnv(DEFAULT_SYSTEM_ENV_NAME);
            } catch (Exception ex) {
                log.error("[PUMP-CONFIG] Cannot load any resources. Cause is '{}'", ex.getLocalizedMessage());
                log.info("[PUMP-CONFIG] Try to load from default Configuration class");
                init(getDefaultConfiguration());
            }
        } else {
            try {
                init(DEFAULT_CLASSPATH_RESOURCE);
            } catch (Exception ex) {
                log.error("[PUMP-CONFIG] Cannot load any resources. Cause is '{}'", ex.getLocalizedMessage());
                log.info("[PUMP-CONFIG] Try to load from default Configuration class");
                init(getDefaultConfiguration());
            }
        }
    }
    //endregion

    private static Configuration getDefaultConfiguration() {
        Configuration config = new Configuration();
        config.setBrowserConfig(BrowserConfig.of(BrowserType.CHROME));
        config.setApplicationName("undefined");
        config.getElement().setWindowWidthOffset(0);
        return config;
    }

    private static void throwException(String type, String path, String scope) {
        log.error("[PUMP-CONFIG] Cannot find correct configuration by '{}' - '{}' in '{}'", type, path, scope);
        throw new ConfigurationException(String.format("Cannot find correct configuration by '%s' - '%s' in '%s'", type, path, scope));
    }

    public static InputStream getResource(String resourceName) {
        ClassLoader loader =
            MoreObjects.firstNonNull(
                Thread.currentThread().getContextClassLoader(), ConfigurationHolder.class.getClassLoader());
        InputStream inputStream = loader.getResourceAsStream(resourceName);
        checkArgument(inputStream != null, "resource %s not found.", resourceName);
        return inputStream;
    }
}
