package ru.mk.pump.commons.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.reporter.AllureReporter;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class StrTest {


    @Test
    void space() {
    }

    @Test
    void space1() {
    }

    @Test
    void concat() {
    }

    @Test
    void mapToPrettyString() {
    }

    @Test
    void mapToPrettyString1() {
    }

    @Test
    void toPrettyString() {
    }

    @Test
    void toPrettyStringOffset() {
        final List<String> list = Lists
                .newArrayList("line1", "line2", "line3", "line4", System.lineSeparator(), System.lineSeparator(), System.lineSeparator());
        log.info(System.lineSeparator() + Str.toPrettyString(list, 2));
        log.info("END");
    }

    @Test
    void match() {
    }

    @Test
    void testToString() {
        log.info(Str.toString(this));
        log.info(Str.toString(Parameter.of("parameter string")));
        log.info(Str.toString(null));
        log.info(Str.toString("string"));
        log.info(Str.toString(10.01));
        log.info(Str.toString(10L));
        log.info(Str.toString(10));
        log.info(Str.toString(new String[]{"string", "string"}));
    }

    @Test
    void trim() {
    }

    @Test
    void exTrim() {
    }

    @Test
    void winFileNormalize() {
    }

    @Test
    void liteNormalize() {
    }

    @Test
    void normalize() {
    }

    @Test
    void isEmpty() {
    }

    @Test
    void toObject() {
        assertThat(Str.toObject("INFO", AllureReporter.Type.class)).isEqualTo(AllureReporter.Type.INFO);
        assertThat(Str.toObject("test", String.class)).isEqualTo("test");
        assertThat(Str.toObject("C:/temp", File.class)).isInstanceOf(File.class);
        assertThat(Str.toObject("java.lang.String", Class.class)).isEqualTo(String.class);
        assertThat(Str.toObject("java.lang.String,java.lang.String", Class[].class)).containsOnly(String.class);
        assertThat(Str.toObject("test1,test2", String[].class)).containsOnly("test1", "test2");

    }

    @SuppressWarnings("UnnecessaryBoxing")
    @Test
    void format() {
        assertThat(Str.format("{} {}", Integer.valueOf(1), null)).isEqualTo("1 null");
        assertThat(Str.format("{} {}", Integer.valueOf(1))).isEqualTo("1 {}");
        assertThat(Str.format(null, Integer.valueOf(1))).isEqualTo("null");
        assertThat(Str.format("{}", (Object) null)).isEqualTo("{}");
    }
}