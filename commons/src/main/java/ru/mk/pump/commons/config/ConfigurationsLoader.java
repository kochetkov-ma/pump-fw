package ru.mk.pump.commons.config;

import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.mk.pump.commons.constants.MainConstants;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.exception.UtilException;
import ru.mk.pump.commons.utils.EnvVariables;
import ru.mk.pump.commons.utils.History;
import ru.mk.pump.commons.utils.History.Info;
import ru.mk.pump.commons.utils.PropertiesUtil;
import ru.mk.pump.commons.utils.Strings;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import static ru.mk.pump.commons.constants.StringConstants.KEY_VALUE_PRETTY_DELIMITER;

@SuppressWarnings({"UnusedReturnValue", "WeakerAccess", "unused"})
@Slf4j
public class ConfigurationsLoader {

    public static final int HISTORY_SIZE = 100;

    //region FIELDS
    private InputStream inputStreamProperties;

    private Path propertiesConfigurationPath;

    private final boolean discoverInEnv;

    private Map<String, String> configMap = Maps.newHashMap();

    private History<String> history = new History<>(100);

    private StringBuilder cache = new StringBuilder();
    //endregion

    //region CONSTRUCTORS
    public ConfigurationsLoader(@NonNull InputStream inputStreamProperties, boolean discoverInEnv) {
        this.inputStreamProperties = inputStreamProperties;
        this.discoverInEnv = discoverInEnv;
    }

    public ConfigurationsLoader(@NonNull InputStream inputStreamProperties) {
        this(inputStreamProperties, true);
    }

    public ConfigurationsLoader(@NonNull Path propertiesConfigurationPath) {
        this(propertiesConfigurationPath, true);
    }

    public ConfigurationsLoader(@NonNull Path propertiesConfigurationPath, boolean discoverInEnv) {

        this.propertiesConfigurationPath = propertiesConfigurationPath;
        this.discoverInEnv = discoverInEnv;
    }
    //endregion

    public static String getHistoryId(@NonNull Class mappableClass, @Nullable String prefix) {
        return Strings.concat("_", mappableClass.getSimpleName(), prefix);
    }

    //region PUBLIC METHODS
    public History<String> getHistory() {
        return history.clone();
    }

    public ConfigurationsLoader load() {
        history.clear();
        if (Objects.nonNull(propertiesConfigurationPath)) {
            configMap = PropertiesUtil.loadMap(propertiesConfigurationPath, MainConstants.FILE_ENCODING);
        } else {
            configMap = PropertiesUtil.loadMap(inputStreamProperties, MainConstants.FILE_ENCODING);
        }
        return this;
    }

    public Map<String, String> getMap() {
        return configMap;
    }

    public <T> T toObject(@NonNull T mappableObject) {
        return toObject(mappableObject, getPrefixOrNull(mappableObject.getClass()));
    }

    public <T> T toObject(@NonNull Class<T> mappableClass) {
        return toObject(mappableClass, getPrefixOrNull(mappableClass));
    }

    public <T> T toObject(@NonNull T mappableObject, @Nullable String prefix) {
        try {
            return resolvedAnnotatedFields(mappableObject, getMap(), prefix);
        } finally {

            history.add(Info.of(getHistoryId(mappableObject.getClass(), prefix), getCache()));
            cache = new StringBuilder();
        }
    }

    public <T> T toObject(@NonNull Class<T> mappableClass, @Nullable String prefix) {
        try {
            return mapToObject(mappableClass, prefix);
        } finally {
            history.add(Info.of(getHistoryId(mappableClass, prefix), getCache()));
            cache = new StringBuilder();
        }

    }

    //endregion

    //region PRIVATE
    private String getCache() {
        return Strings.trim(cache.toString());
    }

    private <T> T mapToObject(@NonNull Class<T> mappableClass, @Nullable String prefix) {
        try {
            return resolvedAnnotatedFields(mappableClass.newInstance(), getMap(), prefix);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UtilException(String.format("Cannot create instance '%s'. Need no args public constructor", mappableClass.getSimpleName()), e);
        }
    }

    private String getPrefixOrNull(Class<?> mappableClass, String... sourcePrefix) {
        final String prePrefix = Strings.concat(StringConstants.DOT, sourcePrefix);
        if (mappableClass.isAnnotationPresent(Config.class)) {
            return Strings.concat(StringConstants.DOT, prePrefix, mappableClass.getAnnotation(Config.class).value());
        } else {
            return prePrefix;
        }
    }

    private String getPrefixOrNull(Field field, String... sourcePrefix) {
        final String prePrefix = Strings.concat(StringConstants.DOT, sourcePrefix);
        if (field.isAnnotationPresent(Property.class)) {
            return Strings.concat(StringConstants.DOT, prePrefix, field.getAnnotation(Property.class).value());
        } else {
            return prePrefix;
        }
    }

    private <T> T resolvedAnnotatedFields(T object, Map<String, String> configMap, String prefix) {
        FieldUtils.getAllFieldsList(object.getClass()).forEach(field ->
                resolvedField(object, field, configMap, prefix));
        return object;
    }

    private void resolvedField(Object object, Field field, Map<String, String> configMap, String prefix) {
        Object result;
        String path;
        String finalPath = "";
        if (field.isAnnotationPresent(Property.class) && !ClassUtils.isPrimitiveOrWrapper(field.getType()) && !field.getType().isAssignableFrom(String.class) && !field.getType().isEnum() && !field.getType().isArray()) {
            try {
                path = getPrefixOrNull(field, prefix);
                result = mapToObject(field.getType(), path);
            } catch (UtilException ex) {
                path = getPrefixOrNull(field);
                result = mapToObject(field.getType(), path);
            }
        } else {
            if (!field.isAnnotationPresent(Property.class)) {
                log.debug("Property annotation is not present in " + field.toString());
                return;
            }

            final Property annotation = field.getAnnotation(Property.class);
            path = annotation.value();
            final String pathAndPrefix = (prefix != null && !prefix.isEmpty() ? prefix + "." : "") + annotation.value();
            final String defaultValue = annotation.defaultValue();
            final boolean isRequired = annotation.required();
            log.debug("Resolving field {}. Annotation {}", field.toString(), annotation.toString());
            if (discoverInEnv && EnvVariables.has(path)) {
                finalPath = "environment var - " + path;
                result = Strings.toObject(EnvVariables.get(path), field.getType());
            } else if (configMap.containsKey(pathAndPrefix)) {
                finalPath = "property - " + pathAndPrefix;
                result = Strings.toObject(configMap.get(pathAndPrefix), field.getType());
            } else if (configMap.containsKey(path)) {
                finalPath = "property - " + path;
                result = Strings.toObject(configMap.get(path), field.getType());
            } else if (!defaultValue.isEmpty()) {
                finalPath = "default value";
                result = Strings.toObject(defaultValue, field.getType());
            } else if (isRequired) {
                throw new UtilException(String.format("Cannot find required property '%s' of field '%s' of object '%s' in file, in system env and default",
                        path, field.getType().getSimpleName(), object.getClass().getSimpleName()));
            } else {
                log.debug("Resolving is nonArg result for not required property {} {}", field.toString(), annotation.toString());
                return;
            }
        }
        try {
            FieldUtils.writeField(field, object, result, true);
            if (!Strings.isEmpty(finalPath)) {
                cache.append(Strings.concat(KEY_VALUE_PRETTY_DELIMITER, finalPath, result.toString())).append(StringConstants.LINE);
            }
            log.debug("Resolving is success. Field {} is {}", field.toString(), result.toString());
        } catch (IllegalAccessException ex) {
            throw new UtilException(String.format("Error writing value '%s' required property '%s' to field '%s' of object '%s'",
                    result.toString(), path, field.getType().getSimpleName(), object.getClass().getSimpleName()), ex);
        }
    }
    //endregion
}