package ru.mk.pump.commons.exception;

import com.google.common.collect.ImmutableMap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.interfaces.StrictInfo;

import java.util.Map;

class PumpExceptionTest {

    @Test
    void test() {
        final PumpException ex = new PumpException("Заголовок")
                .withEnv("имя-1", Info.of())
                .withExtra("имя-1", Info.of());

        Assertions.assertThatThrownBy(() -> {
            throw ex;
        })
                .isInstanceOf(PumpException.class)
                .hasMessageMatching("(?s)Заголовок.*[Additional information].*---имя-1---.*[Environment information].*---имя-1---.*");
    }

    @Test
    void testReorganize() {

        final PumpException causeCause = new PumpException("Заголовок1")
                .withEnv("имя-2", Info.of())
                .withExtra("имя-1", Info.of())
                .withEnv("имя-21", Info.of())
                .withExtra("имя-11", Info.of());

        final PumpException cause = new PumpException("Заголовок2")
                .withCause(causeCause)
                .withEnv("имя-2", Info.of())
                .withExtra("имя-1", Info.of())
                .withEnv("имя-22", Info.of())
                .withExtra("имя-12", Info.of());

        final PumpException ex = new PumpException("Заголовок3", cause)
                .withEnv("имя-2", Info.of())
                .withExtra("имя-1", Info.of())
                .withExtra("имя-10", Info.of());

        Assertions.assertThat(ex.getCause().getMessage())
                .contains("---имя-12---")
                .contains("---имя-22---");

        Assertions.assertThat(ex.getCause().getCause().getMessage())
                .contains("---имя-21---")
                .contains("---имя-2---")
                .contains("---имя-1---")
                .contains("---имя-11---");

        Assertions.assertThatThrownBy(() -> {
            throw ex;
        })
                .isInstanceOf(PumpException.class)
                .hasMessageMatching("(?s)Заголовок3.*[Additional information].*---имя-10---.*");

    }

    private static class Info implements StrictInfo {

        static Info of() {

            return new Info();
        }

        @Override
        public Map<String, String> getInfo() {

            return ImmutableMap.of("key-1", "long long long long long long value-1", "key-2", "long long long long long long long long value-2");
        }
    }

}