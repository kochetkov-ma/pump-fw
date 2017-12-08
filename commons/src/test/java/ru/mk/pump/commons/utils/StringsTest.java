package ru.mk.pump.commons.utils;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.activity.Parameter;

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
        log.info(Strings.toString(new String[]{"string","string"}));
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
}