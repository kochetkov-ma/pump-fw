package ru.mk.pump.commons.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.reporter.ReporterAllure;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class StringsTest {


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
        log.info(System.lineSeparator() + Strings.toPrettyString(list, 2));
        log.info("END");
    }

    @Test
    void match() {
    }

    @Test
    void testToString() {
        log.info(Strings.toString(this));
        log.info(Strings.toString(Parameter.of("parameter string")));
        log.info(Strings.toString(null));
        log.info(Strings.toString("string"));
        log.info(Strings.toString(10.01));
        log.info(Strings.toString(10L));
        log.info(Strings.toString(10));
        log.info(Strings.toString(new String[]{"string", "string"}));
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
        assertThat(Strings.toObject("INFO", ReporterAllure.Type.class)).isEqualTo(ReporterAllure.Type.INFO);
        assertThat(Strings.toObject("test", String.class)).isEqualTo("test");
        assertThat(Strings.toObject("C:/temp", File.class)).isInstanceOf(File.class);
        assertThat(Strings.toObject("java.lang.String", Class.class)).isEqualTo(String.class);
        assertThat(Strings.toObject("java.lang.String,java.lang.String", Class[].class)).containsOnly(String.class);
        assertThat(Strings.toObject("test1,test2", String[].class)).containsOnly("test1", "test2");

    }
}