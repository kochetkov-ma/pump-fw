package ru.mk.pump.commons.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.exception.UtilException;

import java.util.Objects;

@SuppressWarnings("WeakerAccess")
@UtilityClass
@Slf4j
public final class RandomUtil {

    private static final String RUSSIAN_CODE = "+7";

    public static final RandomStringGenerator.Builder RUSSIAN = new RandomStringGenerator.Builder().withinRange('А', 'я')
            .filteredBy(CharacterPredicates.LETTERS);

    public static final RandomStringGenerator.Builder ENGLISH = new RandomStringGenerator.Builder().withinRange('A', 'z')
            .filteredBy(CharacterPredicates.LETTERS);

    public static final RandomStringGenerator.Builder NUMBER = new RandomStringGenerator.Builder().withinRange('0', '9')
            .filteredBy(CharacterPredicates.DIGITS);

    public static String newNumber(int len) {
        return NUMBER.build().generate(len);
    }

    public static String newEnglishString(int len) {
        return ENGLISH.build().generate(len);
    }

    public static String newRussianString(int len) {
        return RUSSIAN.build().generate(len);
    }

    /**
     * +79207101122 - 12 chars
     */
    public String newRussianMobilePhone(@Nullable String fullPrefixWithCode) {
        if (Strings.isEmpty(fullPrefixWithCode)) {
            fullPrefixWithCode = RUSSIAN_CODE;
        } else if (!fullPrefixWithCode.startsWith("+")) {
            if (!fullPrefixWithCode.startsWith("7")) {
                fullPrefixWithCode = RUSSIAN_CODE + fullPrefixWithCode;
            } else {
                fullPrefixWithCode = "+" + fullPrefixWithCode;
            }
        }
        if (fullPrefixWithCode.length() > 12) {
            throw new UtilException("Cannot generate new phone for prefix size more 12 chars. Actual prefix " + fullPrefixWithCode);
        }
        return fullPrefixWithCode + newNumber(12 - fullPrefixWithCode.length());
    }
}
