package ru.mk.pump.commons.utils;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.propertyeditors.*;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.exception.UtilException;
import ru.mk.pump.commons.interfaces.PrettyPrinter;

import javax.annotation.Nullable;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.mk.pump.commons.constants.StringConstants.KEY_VALUE_PRETTY_DELIMITER;
import static ru.mk.pump.commons.constants.StringConstants.LINE;

@SuppressWarnings({"unused", "WeakerAccess"})
@UtilityClass
public class Str {

    public static final String LITE_NORMALIZE = "[\\n\\t]";
    public static final String NORMALIZE = "[-!—+.^:(),\\s\\n\\t]";
    public static final String WIN_FILE_NORMALIZE = "[\\\\/:*?<>|]";

    static {
        PropertyEditorManager.registerEditor(String[].class, StringArrayPropertyEditor.class);
        PropertyEditorManager.registerEditor(char[].class, CharArrayPropertyEditor.class);
        PropertyEditorManager.registerEditor(Class[].class, ClassArrayEditor.class);
        PropertyEditorManager.registerEditor(Class.class, ClassEditor.class);
        PropertyEditorManager.registerEditor(File.class, FileEditor.class);
        PropertyEditorManager.registerEditor(Path.class, PathEditor.class);
        PropertyEditorManager.registerEditor(UUID.class, UUIDEditor.class);
        PropertyEditorManager.registerEditor(URL.class, URLEditor.class);
        PropertyEditorManager.registerEditor(URI.class, URIEditor.class);
    }

    /**
     * Null safe formatter like in slf4j.
     *
     * @param slf4jMessagePattern Slf4j pattern with '{}'
     * @param args                Values
     *
     * @return Formatted string
     */
    public String format(@Nullable String slf4jMessagePattern, @Nullable Object... args) {
        if (slf4jMessagePattern == null) {
            return "null";
        }
        if (args == null) {
            return slf4jMessagePattern;
        }
        return MessageFormatter.arrayFormat(slf4jMessagePattern, args).getMessage();
    }

    public String empty() {
        return StringConstants.EMPTY;
    }

    public String line() {
        return StringConstants.LINE;
    }

    public String space(int count) {
        final StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0, count).forEach((index) -> stringBuilder.append(StringConstants.SPACE));
        return stringBuilder.toString();
    }

    public String line(String... strings) {
        return trim(Arrays.stream(strings).filter(i -> i != null && !i.isEmpty()).collect(Collectors.joining(StringConstants.LINE)));
    }

    public String space(String... strings) {
        return trim(Arrays.stream(strings).filter(i -> i != null && !i.isEmpty()).collect(Collectors.joining(StringConstants.SPACE)));
    }

    public String concat(CharSequence delimiter, String... strings) {
        return trim(Arrays.stream(strings).filter(i -> i != null && !i.isEmpty()).collect(Collectors.joining(delimiter)));
    }

    public String toPrettyString(@Nullable Map<?, ?> map) {
        return toPrettyString(map, 0);
    }

    public String toPrettyString(@Nullable Map<?, ?> map, int offset) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (map == null) {
            return "null";
        }
        map.forEach((key, value) -> {
            if (key != null && value != null) {
                stringBuilder.append(key.toString()).append(KEY_VALUE_PRETTY_DELIMITER).append(Str.toPrettyString(value)).append(LINE);
                if (offset != 0) {
                    stringBuilder.append(space(offset + StringConstants.KEY_VALUE_PRETTY_DELIMITER.length()));
                }
            }
        });
        return trimEnd(stringBuilder.toString());
    }

    public String toPrettyString(@Nullable Collection<?> collection) {
        return toPrettyString(collection, 0);
    }

    public String toPrettyString(@Nullable Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof PrettyPrinter) {
            return ((PrettyPrinter) object).toPrettyString();
        }
        return Str.toString(object);
    }

    public String toPrettyString(@Nullable Object[] array) {
        return toPrettyString(array, 0);
    }

    public String toPrettyString(@Nullable Object[] array, int offset) {
        if (array == null) {
            return "null";
        }
        return toPrettyString(Arrays.asList(array));
    }

    public String toPrettyString(@Nullable Collection<?> collection, int offset) {
        final StringBuilder sb = new StringBuilder();
        if (collection == null) {
            return "null";
        }
        collection.forEach((value) -> {
            if (value != null) {
                if (offset != 0) {
                    sb.append(space(offset));
                }
                sb.append(Str.toPrettyString(value));
                if (!StringUtils.endsWith(Str.toPrettyString(value), LINE)) {
                    sb.append(LINE);
                }
            }
        });
        return trimEnd(sb.toString());
    }

    public boolean match(@NonNull String regEx, String actual) {
        return actual != null && actual.matches(regEx);
    }

    public String trimEnd(String string) {
        return org.apache.commons.lang3.StringUtils.stripEnd(string, LINE + " ");
    }

    public String trim(String string) {
        return org.apache.commons.lang3.StringUtils.trim(string);
    }

    public static String exTrim(String value) {
        if (value == null) {
            return "";
        }
        return trim(value.replaceAll("\\s{2,}", " "));
    }

    public static String winFileNormalize(String valueWithBadSymbols, char characterForReplaceBadSymbols) {
        if (valueWithBadSymbols == null) {
            return "";
        }
        return trim(valueWithBadSymbols.toLowerCase().replaceAll(WIN_FILE_NORMALIZE, Character.toString(characterForReplaceBadSymbols)));
    }

    public String liteNormalize(String value) {
        if (value == null) {
            return "";
        }
        return trim(value.toLowerCase().replaceAll(LITE_NORMALIZE, ""));
    }

    public String normalize(String value) {
        if (value == null) {
            return "";
        }
        return trim(value.toLowerCase().replaceAll(NORMALIZE, ""));
    }

    public String toString(Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof Collection) {
            return trim(Arrays.toString(((Collection) object).toArray()));
        }
        if (object instanceof Object[]) {
            return Arrays.toString((Object[]) object);
        }
        try {
            if (object.getClass().getMethod("toString").getDeclaringClass() != Object.class) {
                return object.toString();
            }
        } catch (NoSuchMethodException ignore) {
        }
        return ReflectionToStringBuilder.toString(object, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * Check null or nonArg
     *
     * @return true if null or nonArg
     */
    public boolean isEmpty(@Nullable String value) {
        return StringUtils.isEmpty(value);
    }

    public void ifNotEmptyOrBlank(@Nullable String value, Consumer<String> consumer) {
        if (!isBlank(value)) {
            consumer.accept(value);
        }
    }

    /**
     * Check null or blank or only whitespace
     *
     * @return true if null or nonArg or only whitespace
     */
    public boolean isBlank(@Nullable String value) {
        return StringUtils.isBlank(value);
    }

    @Nullable
    public <T> T toObject(@NonNull String string, @NonNull Class<T> targetType) {
        Preconditions.checkNotNull(string);
        Preconditions.checkNotNull(targetType);
        final PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        if (editor == null) {
            throw new UtilException(String.format("Cannot find PropertyEditor of '%s'", targetType));
        }
        try {
            editor.setAsText(string);
        } catch (Exception ignore) {
            return null;
        }
        //noinspection unchecked
        return (T) editor.getValue();
    }
}