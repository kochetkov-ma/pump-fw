package ru.mk.pump.commons.utils;

import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.mk.pump.commons.interfaces.Collator;

@UtilityClass
public class Collators {

    public Collator<Object> equals() {
        return new Collator<Object>() {

            private Object expected;

            private Object actual;

            @Override
            public boolean collate(Object expected, Object actual) {
                this.expected = expected;
                this.actual = actual;
                return Objects.equals(expected, actual);
            }

            @Override
            public String getMessage() {
                return Strings.oneLineConcat(actual(actual), "равно ожидаемому значению", info(expected));
            }
        };
    }

    public Collator<String> contains() {
        return new Collator<String>() {

            private String expected;

            private String actual;

            @Override
            public boolean collate(String expected, String actual) {
                this.expected = expected;
                this.actual = actual;
                return StringUtils.contains(this.actual, this.expected);
            }

            @Override
            public String getMessage() {
                return Strings.oneLineConcat(actual(actual), "содержит ожидаемую строку", info(expected));
            }
        };
    }

    /**
     * use {@link Strings#liteNormalize(String)} for actual and expected
     */
    public Collator<String> liteNormalizeContains() {
        return new Collator<String>() {

            private String expected;

            private String actual;

            @Override
            public String handle(String value) {
                return Strings.liteNormalize(value);
            }

            @Override
            public boolean collate(String expected, String actual) {
                this.expected = handle(expected);
                this.actual = handle(actual);
                return StringUtils.contains(this.actual, this.expected);
            }

            @Override
            public String getMessage() {
                return Strings.oneLineConcat(actual(actual), "содержит ожидаемую строку", info(expected));
            }
        };
    }

    /**
     * use {@link Strings#normalize(String)} for actual and expected
     */
    public Collator<String> normalizeContains() {
        return new Collator<String>() {

            private String expected;

            private String actual;

            @Override
            public String handle(String value) {
                return Strings.normalize(value);
            }

            @Override
            public boolean collate(String expected, String actual) {
                this.expected = handle(expected);
                this.actual = handle(actual);
                return StringUtils.contains(this.actual, this.expected);
            }

            @Override
            public String getMessage() {
                return Strings.oneLineConcat(actual(actual), "содержит ожидаемую строку", info(expected));
            }
        };
    }
}
