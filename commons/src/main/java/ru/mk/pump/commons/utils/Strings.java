package ru.mk.pump.commons.utils;


import static ru.mk.pump.commons.constants.StringConstants.KEY_VALUE_PRETTY_DELIMITER;
import static ru.mk.pump.commons.constants.StringConstants.LINE;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.constants.StringConstants;

@SuppressWarnings({"unused", "WeakerAccess"})
@UtilityClass
public class Strings {

    public static final String EMPTY = "";

    public static final String LITE_NORMALIZE = "[\\n\\t]";

    public static final String NORMALIZE = "[-+.^:(),\\s\\n\\t]";

    public static final String WIN_FILE_NORMALIZE = "[\\\\/:*?<>|]";

    public String empty(){
        return EMPTY;
    }

    public String space(int count) {
        final StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0, count).forEach((index) -> stringBuilder.append(StringConstants.SPACE));
        return stringBuilder.toString();
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
                stringBuilder.append(key.toString()).append(KEY_VALUE_PRETTY_DELIMITER).append(value.toString()).append(LINE);
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
                sb.append(value.toString());
                if (!StringUtils.contains(value.toString(), LINE)) {
                    sb.append(LINE);
                }
            }
        });
        return trimEnd(sb.toString());
    }

    public boolean match(@NotNull String regEx, String actual) {
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
        try {
            if (object.getClass().getMethod("toString").getDeclaringClass() != Object.class) {
                return object.toString();
            }
        } catch (NoSuchMethodException ignore) {
        }
        return ReflectionToStringBuilder.toString(object, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * Check null or empty
     * @return true if null or empty
     */
    public boolean isEmpty(@Nullable String value) {
        return StringUtils.isEmpty(value);
    }

    /**
     * Check null or blank or only whitespace
     * @return true if null or empty or only whitespace
     */
    public boolean isBlank(@Nullable String value) {
        return StringUtils.isBlank(value);
    }
}
