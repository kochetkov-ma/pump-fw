package ru.mk.pump.commons.utils;

import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.mk.pump.commons.interfaces.Collator;

@UtilityClass
public class Collators {

    public <T> Collator<T> equals() {
        return new Collator<T>() {

            private T expected;

            private T actual;

            @Override
            public boolean collate(T expected, T actual) {
                this.expected = expected;
                this.actual = actual;
                return Objects.equals(expected, actual);
            }

            @Override
            public String getMessage() {
                return Str.space(actual(actual), "равно ожидаемому значению", info(expected));
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
                return Str.space(actual(actual), "содержит ожидаемую строку", info(expected));
            }
        };
    }

    public Collator<String> containsReverse() {
        return new Collator<String>() {

            private String expected;

            private String actual;

            @Override
            public boolean collate(String expected, String actual) {
                this.expected = expected;
                this.actual = actual;
                return StringUtils.contains(this.expected, this.actual);
            }

            @Override
            public String getMessage() {
                return Str.space(actual(actual), "содержится в ожидаемой строке", info(expected));
            }
        };
    }

    /**
     * use {@link Str#liteNormalize(String)} for actual and expected
     */
    public Collator<String> liteNormalizeContains() {
        return new Collator<String>() {

            private String expected;

            private String actual;

            @Override
            public String handle(String value) {
                return Str.liteNormalize(value);
            }

            @Override
            public boolean collate(String expected, String actual) {
                this.expected = handle(expected);
                this.actual = handle(actual);
                return StringUtils.contains(this.actual, this.expected);
            }

            @Override
            public String getMessage() {
                return Str.space(actual(actual), "содержит ожидаемую строку", info(expected));
            }
        };
    }

    /**
     * use {@link Str#normalize(String)} for actual and expected
     */
    public Collator<String> normalizeContains() {
        return new Collator<String>() {

            private String expected;

            private String actual;

            @Override
            public String handle(String value) {
                return Str.normalize(value);
            }

            @Override
            public boolean collate(String expected, String actual) {
                this.expected = handle(expected);
                this.actual = handle(actual);
                return StringUtils.contains(this.actual, this.expected);
            }

            @Override
            public String getMessage() {
                return Str.space(actual(actual), "содержит ожидаемую строку", info(expected));
            }
        };
    }
}
