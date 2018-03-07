package ru.mk.pump.commons.config;

import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.exception.ConfigurationException;
import ru.mk.pump.commons.utils.EnvVariables;
import ru.mk.pump.commons.utils.FileUtils;
import ru.mk.pump.commons.utils.Preconditions;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
@Slf4j
@ToString
public class ConfigurationHelper<T> {

    private final String sysEnvName;

    private final String classPathResource;

    private final T defaultConfig;

    @Getter
    @Setter
    private volatile T actualConfig;

    private ConfigurationsLoader configurationsLoader;

    public ConfigurationHelper(@Nullable String sysEnvName, @Nullable String classPathResource, @NonNull T defaultConfig) {
        this.sysEnvName = sysEnvName;
        this.classPathResource = classPathResource;
        this.defaultConfig = defaultConfig;
    }

    @Nullable
    public T config() {
        return actualConfig;
    }

    public Optional<ConfigurationsLoader> getLoader() {
        return Optional.ofNullable(configurationsLoader);
    }

    public synchronized T loadFromLoader(@NonNull ConfigurationsLoader configurationsLoader) {
        this.configurationsLoader = configurationsLoader;
        configurationsLoader.load();
        actualConfig = configurationsLoader.toObject(getType());
        return actualConfig;
    }

    public synchronized T loadFromStream(@NonNull InputStream inputStream) {
        log.info("[PUMP-CONFIG] Try to load from configuration InputStream");
        return loadFromLoader(new ConfigurationsLoader(inputStream));
    }

    public synchronized T loadFromResource(@NonNull String resourcePath) {
        log.info("[PUMP-CONFIG] Try to load classpath configuration from '{}'", resourcePath);
        if (FileUtils.isExistsAndValid(resourcePath)) {
            //noinspection ConstantConditions
            return loadFromFile(Paths.get(resourcePath));
        } else {
            return loadFromStream(getResource(resourcePath));
        }
    }

    public synchronized T loadFromFile(@NonNull Path path) {
        log.info("[PUMP-CONFIG] Try to load file system path configuration from '{}'", path);
        if (Files.notExists(path)) {
            throw exception("path", path.toString(), "file system");
        }
        return loadFromLoader(new ConfigurationsLoader(path));
    }

    public synchronized T loadFromSystemEnv(@NonNull String envVarWithResourceNameInClassPath) {
        log.info("[PUMP-CONFIG] Try to load environment variables configuration from '{}'", envVarWithResourceNameInClassPath);
        Preconditions.checkStringNotBlank(envVarWithResourceNameInClassPath);
        if (EnvVariables.has(envVarWithResourceNameInClassPath)) {
            //noinspection ConstantConditions
            return loadFromResource(EnvVariables.get(envVarWithResourceNameInClassPath));
        }
        throw exception("name", envVarWithResourceNameInClassPath, "environment variables");
    }

    @NonNull
    public synchronized T loadAuto() {
        log.info("[PUMP-CONFIG] Try to load default configuration");
        if (EnvVariables.has(sysEnvName)) {
            try {
                return loadFromSystemEnv(sysEnvName);
            } catch (Exception ex) {
                log.error("[PUMP-CONFIG] Cannot load any resources. Cause is '{}'", ex.toString());
                log.info("[PUMP-CONFIG] Try to load from default Configuration class");
                actualConfig = defaultConfig;
                return actualConfig;

            }
        } else {
            try {
                return loadFromResource(classPathResource);
            } catch (Exception ex) {
                log.error("[PUMP-CONFIG] Cannot load any resources. Cause is '{}'", ex.toString());
                log.info("[PUMP-CONFIG] Try to load from default Configuration class");
                actualConfig = defaultConfig;
                return actualConfig;
            }
        }
    }

    private InputStream getResource(String resourceName) {
        ClassLoader loader =
                MoreObjects.firstNonNull(
                        Thread.currentThread().getContextClassLoader(), getClass().getClassLoader());
        InputStream inputStream = loader.getResourceAsStream(resourceName);
        checkArgument(inputStream != null, "resource %s not found.", resourceName);
        return inputStream;
    }

    private Class<T> getType() {
        //noinspection unchecked
        return (Class<T>) defaultConfig.getClass();
    }

    private RuntimeException exception(String type, String path, String scope) {
        log.error("[PUMP-CONFIG] Cannot find correct configuration by '{}' - '{}' in '{}'", type, path, scope);
        return new ConfigurationException(String.format("Cannot find correct configuration by '%s' - '%s' in '%s'", type, path, scope));
    }
}
