package ru.mk.pump.commons.utils;


import static ru.mk.pump.commons.constants.StringConstants.KEY_VALUE_PRETTY_DELIMITER;
import static ru.mk.pump.commons.constants.StringConstants.LINE;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.constants.StringConstants;

@SuppressWarnings({"unused", "WeakerAccess"})
@UtilityClass
public class Strings {

    public static String LITE_NORMALIZE = "[\\n\\t]";

    public static String NORMALIZE = "[-+.^:(),\\s\\n\\t]";

    public static String WIN_FILE_NORMALIZE = "[\\\\/:*?<>|]";

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

    public String mapToPrettyString(Map<?, ?> map) {
        final StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> stringBuilder.append(key.toString()).append(KEY_VALUE_PRETTY_DELIMITER).append(value.toString()).append(LINE));
        return trim(stringBuilder.toString());
    }

    public String mapToPrettyString(Map<?, ?> map, int offset) {
        final StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> stringBuilder.append(key.toString()).append(KEY_VALUE_PRETTY_DELIMITER).append(value.toString()).append(LINE)
            .append(space(offset)));
        return trim(stringBuilder.toString());
    }

    public String toPrettyString(Collection<?> collection) {
        final StringBuilder sb = new StringBuilder();
        collection.forEach((value) -> sb.append(value.toString()).append(LINE));
        return trim(sb.toString());
    }

    public String toPrettyString(Collection<?> collection, int offset) {
        final StringBuilder sb = new StringBuilder();
        collection.forEach((value) -> sb.append(value.toString()).append(LINE).append(space(offset + 3)));
        return trim(sb.toString());
    }

    public boolean match(@NotNull String regEx, String actual) {
        return actual != null && actual.matches(regEx);
    }

    public String listToString(List<?> list) {
        return trim(Arrays.toString(list.toArray()));
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

    public boolean isEmpty(String value) {
        return com.google.common.base.Strings.isNullOrEmpty(value);
    }

}
