package ru.mk.pump.commons.utils;

import static ru.mk.pump.commons.constants.StringConstants.KEY_VALUE_PRETTY_DELIMITER;
import static ru.mk.pump.commons.constants.StringConstants.LINE;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import ru.mk.pump.commons.constants.StringConstants;

@SuppressWarnings("unused")
@UtilityClass
public class StringUtils {

    /**
     * Used Collectors.joining. null values were not added
     */
    public String oneLineConcat(String... strings) {
        return Arrays.stream(strings).filter(i -> i != null && !i.isEmpty()).collect(Collectors.joining(StringConstants.SPACE));
    }

    public String concat(CharSequence delimiter, String... strings) {
        return Arrays.stream(strings).filter(i -> i != null && !i.isEmpty()).collect(Collectors.joining(delimiter));
    }

    public String mapToPrettyString(Map<?, ?> map) {
        final StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> stringBuilder.append(key.toString()).append(KEY_VALUE_PRETTY_DELIMITER).append(value.toString()).append(LINE));
        return stringBuilder.toString();
    }

    public String listToPrettyString(List<?> list) {
        final StringBuilder sb = new StringBuilder();
        list.forEach((value) -> sb.append(value.toString()).append(LINE));
        return sb.toString();
    }

    public String listToString(List<?> list) {
        return Arrays.toString(list.toArray());
    }
}
