package ru.mk.pump.commons.utils;

import lombok.experimental.UtilityClass;
import ru.mk.pump.commons.exception.UtilException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
@UtilityClass
public class PropertiesUtil {

    public Properties load(Path propertiesPath, Charset charset) {
        final Properties result = new Properties();
        try (Reader reader = Files.newBufferedReader(propertiesPath, charset)) {
            result.load(reader);
        } catch (IOException ex) {
            throw new UtilException("Error loading properties " + propertiesPath, ex);
        }
        return result;
    }

    public Map<String, String> loadMap(Path propertiesPath, Charset charset) {
        return propertiesToMap(load(propertiesPath, charset));
    }

    public Map<String, String> propertiesToMap(Properties properties) {
        return properties.entrySet().stream()
                .collect(Collectors.toMap(entry -> Objects.toString(entry.getKey()), e -> String.valueOf(e.getValue())));
    }
}
