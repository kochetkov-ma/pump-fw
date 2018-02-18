package ru.mk.pump.commons.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.reporter.ReporterAllure;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class StringsTest {


    @Test
    public void space() {
    }

    @Test
    public void space1() {
    }

    @Test
    public void concat() {
    }

    @Test
    public void mapToPrettyString() {
    }

    @Test
    public void mapToPrettyString1() {
    }

    @Test
    public void toPrettyString() {
    }

    @Test
    public void toPrettyStringOffset() {
        final List<String> list = Lists
                .newArrayList("line1", "line2", "line3", "line4", System.lineSeparator(), System.lineSeparator(), System.lineSeparator());
        log.info(System.lineSeparator() + Strings.toPrettyString(list, 2));
        log.info("END");
    }

    @Test
    public void match() {
    }

    @Test
    public void testToString() {
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
    public void trim() {
    }

    @Test
    public void exTrim() {
    }

    @Test
    public void winFileNormalize() {
    }

    @Test
    public void liteNormalize() {
    }

    @Test
    public void normalize() {
    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void toObject() {
        assertThat(Strings.toObject("INFO", ReporterAllure.Type.class)).isEqualTo(ReporterAllure.Type.INFO);
        assertThat(Strings.toObject("test", String.class)).isEqualTo("test");
        assertThat(Strings.toObject("C:/temp", File.class)).isInstanceOf(File.class);
        assertThat(Strings.toObject("java.lang.String", Class.class)).isEqualTo(String.class);
        assertThat(Strings.toObject("java.lang.String,java.lang.String", Class[].class)).containsOnly(String.class);
        assertThat(Strings.toObject("test1,test2", String[].class)).containsOnly("test1", "test2");

    }
}