package ru.mk.pump.commons.exception;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.junit.Test;
import ru.mk.pump.commons.interfaces.StrictInfo;

public class PumpExceptionTest {

    private static class Info implements StrictInfo{

        static Info of(){
            return new Info();
        }

        @Override
        public Map<String, String> getInfo() {
            return ImmutableMap.of("key-1", "long long long long long long value-1", "key-2", "long long long long long long long long value-2");
        }
    }

    @Test
    public void test(){
        PumpException ex = new PumpException("Заголовок");
        ex.addEnv("имя-1", Info.of());
        ex.addTarget("имя-1", Info.of());
        throw ex;
    }

    @Test
    public void testReorganize(){
        PumpException causeCause = new PumpException("Заголовок1");
        causeCause.addEnv("имя-2", Info.of());
        causeCause.addTarget("имя-1", Info.of());
        causeCause.addEnv("имя-21", Info.of());
        causeCause.addTarget("имя-11", Info.of());

        PumpException cause = new PumpException("Заголовок2", causeCause);
        cause.addEnv("имя-2", Info.of());
        cause.addTarget("имя-1", Info.of());
        cause.addEnv("имя-22", Info.of());
        cause.addTarget("имя-12", Info.of());

        PumpException ex = new PumpException("Заголовок3", cause);
        ex.addEnv("имя-2", Info.of());
        ex.addTarget("имя-1", Info.of());
        ex.addTarget("имя-10", Info.of());
        throw ex;
    }

}