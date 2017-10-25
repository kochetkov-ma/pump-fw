package ru.mk.pump.commons.config;

import com.google.common.collect.Maps;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.constants.MainConstants;
import ru.mk.pump.commons.exception.UtilException;
import ru.mk.pump.commons.utils.EnvVariables;
import ru.mk.pump.commons.utils.PropertiesUtil;

@SuppressWarnings("UnusedReturnValue")
@Slf4j
public class ConfigurationsLoader {

    //region FIELDS
    private final Path propertiesConfigurationPath;

    private final boolean discoverInEnv;

    private Map<String, String> configMap = Maps.newHashMap();
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
    public ConfigurationsLoader load() {
        configMap = PropertiesUtil.loadMap(propertiesConfigurationPath, MainConstants.FILE_ENCODING);
        return this;
    }

    public Map<String, String> getMap() {
        return configMap;
    }

    public <T> T toObject(@NotNull T mappableObject, @Nullable String standName) {

        return resolvedAnnotatedFields(mappableObject, getMap(), standName);
    }

    public <T> T toObject(@NotNull Class<T> mappableClass, @Nullable String standName) {
        try {
            return toObject(mappableClass.newInstance(), standName);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UtilException(String.format("Cannot create instance '%s'. Need no args public constructor", mappableClass.getSimpleName()), e);
        }
    }
    //endregion

    //region PRIVATE
    private <T> T resolvedAnnotatedFields(T object, Map<String, String> configMap, String prefix) {
        FieldUtils.getFieldsListWithAnnotation(object.getClass(), Property.class).forEach(field ->
            resolvedField(object, field, configMap, prefix));
        return object;
    }

    private void resolvedField(Object object, Field field, Map<String, String> configMap, String prefix) {
        if (!field.isAnnotationPresent(Property.class)) {
            log.debug("Property annotation is not present in " + field.toString());
            return;
        }

        final Property annotation = field.getAnnotation(Property.class);
        final String path = field.getAnnotation(Property.class).value();
        final String pathAndPrefix = (prefix != null && !prefix.isEmpty() ? prefix + "." : "") + field.getAnnotation(Property.class).value();
        final String defaultValue = field.getAnnotation(Property.class).defaultValue();
        final boolean isRequired = field.getAnnotation(Property.class).required();
        final PropertyEditor editor = PropertyEditorManager.findEditor(field.getType());
        final Object result;

        log.debug("Resolving field {}. Annotation {}", field.toString(), annotation.toString());
        if (discoverInEnv && EnvVariables.has(path)) {
            editor.setAsText(EnvVariables.get(path));
            result = editor.getValue();
        } else if (configMap.containsKey(pathAndPrefix)) {
            editor.setAsText(configMap.get(pathAndPrefix));
            result = editor.getValue();
        } else if (configMap.containsKey(path)) {
            editor.setAsText(configMap.get(path));
            result = editor.getValue();
        } else if (!defaultValue.isEmpty()) {
            editor.setAsText(defaultValue);
            result = editor.getValue();
        } else if (isRequired) {
            throw new UtilException(String.format("Cannot find required property '%s' of field '%s' of object '%s' in file, in system env and default",
                path, field.getType().getSimpleName(), object.getClass().getSimpleName()));
        } else {
            log.debug("Resolving is empty result for not required property {} {}", field.toString(), annotation.toString());
            return;
        }

        try {
            FieldUtils.writeField(field, object, result, true);
            log.debug("Resolving is success. Field {} is {}", field.toString(), result.toString());
        } catch (IllegalAccessException ex) {
            throw new UtilException(String.format("Error writing value '%s' required property '%s' to field '%s' of object '%s'",
                result.toString(), path, field.getType().getSimpleName(), object.getClass().getSimpleName()), ex);
        }
    }
    //endregion
}