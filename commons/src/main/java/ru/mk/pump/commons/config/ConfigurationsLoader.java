package ru.mk.pump.commons.config;

import static ru.mk.pump.commons.constants.StringConstants.KEY_VALUE_PRETTY_DELIMITER;

import com.google.common.collect.Maps;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.constants.MainConstants;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.exception.UtilException;
import ru.mk.pump.commons.utils.EnvVariables;
import ru.mk.pump.commons.utils.History;
import ru.mk.pump.commons.utils.History.Info;
import ru.mk.pump.commons.utils.PropertiesUtil;
import ru.mk.pump.commons.utils.Strings;

@SuppressWarnings("UnusedReturnValue")
@Slf4j
public class ConfigurationsLoader {

    public static final int HISTORY_SIZE = 100;

    //region FIELDS
    private final Path propertiesConfigurationPath;

    private final boolean discoverInEnv;

    private Map<String, String> configMap = Maps.newHashMap();

    private History<String> history = new History<>(100);

    private StringBuilder cache = new StringBuilder();
    //endregion

    //region CONSTRUCTORS
    public ConfigurationsLoader(@NotNull Path propertiesConfigurationPath) {
        this(propertiesConfigurationPath, true);
    }

    public ConfigurationsLoader(@NotNull Path propertiesConfigurationPath, boolean discoverInEnv) {

        this.propertiesConfigurationPath = propertiesConfigurationPath;
        this.discoverInEnv = discoverInEnv;
    }
    //endregion

    //region PUBLIC METHODS
    public History<String> getHistory(){
        return history.clone();
    }

    public ConfigurationsLoader load() {
        history.clear();
        configMap = PropertiesUtil.loadMap(propertiesConfigurationPath, MainConstants.FILE_ENCODING);
        return this;
    }

    public Map<String, String> getMap() {
        return configMap;
    }

    public <T> T toObject(@NotNull T mappableObject) {
        return toObject(mappableObject, getPrefixOrNull(mappableObject.getClass()));
    }

    public <T> T toObject(@NotNull Class<T> mappableClass) {
        return toObject(mappableClass, getPrefixOrNull(mappableClass));
    }

    public <T> T toObject(@NotNull T mappableObject, @Nullable String prefix) {
        try {
            return resolvedAnnotatedFields(mappableObject, getMap(), prefix);
        } finally {

            history.add(Info.of(getHistoryId(mappableObject.getClass(), prefix), getCache()));
            cache = new StringBuilder();
        }
    }

    public <T> T toObject(@NotNull Class<T> mappableClass, @Nullable String prefix) {
        try {
            return mapToObject(mappableClass, prefix);
        } finally {
            history.add(Info.of(getHistoryId(mappableClass, prefix), getCache()));
            cache = new StringBuilder();
        }

    }

    public static String getHistoryId(@NotNull Class mappableClass, @Nullable String prefix) {
        return Strings.concat("_", mappableClass.getSimpleName(), prefix);
    }

    //endregion

    //region PRIVATE
    private String getCache() {
        return Strings.trim(cache.toString());
    }

    private <T> T mapToObject(@NotNull Class<T> mappableClass, @Nullable String prefix) {
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
        if (field.isAnnotationPresent(Config.class)) {
            return Strings.concat(StringConstants.DOT, prePrefix, field.getAnnotation(Config.class).value());
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
        if (field.isAnnotationPresent(Config.class)) {
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
            final PropertyEditor editor = PropertyEditorManager.findEditor(field.getType());

            log.debug("Resolving field {}. Annotation {}", field.toString(), annotation.toString());
            if (discoverInEnv && EnvVariables.has(path)) {
                editor.setAsText(EnvVariables.get(path));
                finalPath = "environment var - " + path;
                result = editor.getValue();
            } else if (configMap.containsKey(pathAndPrefix)) {
                editor.setAsText(configMap.get(pathAndPrefix));
                finalPath = "property - " + pathAndPrefix;
                result = editor.getValue();
            } else if (configMap.containsKey(path)) {
                editor.setAsText(configMap.get(path));
                finalPath = "property - " + path;
                result = editor.getValue();
            } else if (!defaultValue.isEmpty()) {
                editor.setAsText(defaultValue);
                finalPath = "default value";
                result = editor.getValue();
            } else if (isRequired) {
                throw new UtilException(String.format("Cannot find required property '%s' of field '%s' of object '%s' in file, in system env and default",
                    path, field.getType().getSimpleName(), object.getClass().getSimpleName()));
            } else {
                log.debug("Resolving is empty result for not required property {} {}", field.toString(), annotation.toString());
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