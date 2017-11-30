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

}