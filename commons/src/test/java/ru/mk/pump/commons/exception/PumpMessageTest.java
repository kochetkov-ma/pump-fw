package ru.mk.pump.commons.exception;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.utils.Strings;

@Slf4j
public class PumpMessageTest {

    @Test
    public void test() {
        final Map<String, String> map1 = ImmutableMap
            .of("key-1", "long long long long long long value-1", "key-2", "long long long long long long long long value-2");
        final Map<String, String> map2 = ImmutableMap.of("key-1", "value-1", "key-2", Strings.toPrettyString(map1));
        final Map<String, String> map3 = ImmutableMap.of("key-1", "value-1", "key-2", "value-2");
        final Map<String, String> map4 = ImmutableMap.of("key-1", "value-1", "key-2", "value-2");

        PumpMessage msg = new PumpMessage("Заголовок")
            .withPre("PRE")
            .withDesc(System.lineSeparator() + "Описание" + System.lineSeparator())
            .addExtraInfo(map1)
            .addExtraInfo(map2)
            .addExtraInfo(map3)
            .addExtraInfo(map4)

            .addEnvInfo(map1)
            .addEnvInfo(map2)
            .addEnvInfo(map3)
            .addEnvInfo(map4);
        log.info(msg.toPrettyString());
        log.info("END");
    }

    @Test
    public void testShort() {

        PumpMessage msg = new PumpMessage("Заголовок");

        log.info(msg.toPrettyString());
        log.info("END");
    }

}