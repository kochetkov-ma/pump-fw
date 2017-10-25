package ru.mk.pump.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import ru.mk.pump.commons.utils.Resources;

@Slf4j
public class ConfigurationsLoaderTest {

    @Test
    public void testLoad() {
    }

    @Test
    public void testGetMap() {
    }

    @Test
    public void testMapToObject() {
        System.setProperty("common.extra", "from_env");
        ConfigurationsLoader configurationsLoader = new ConfigurationsLoader(Resources.findResource("stand.properties"), true);
        configurationsLoader.load();
        final Stand stand = configurationsLoader.toObject(Stand.class, "main");
        Assert.assertEquals("Не установилось свойство common", stand.getCommon(), "common");
        Assert.assertEquals("Не установилось свойство extra", stand.getExtra(), "from_env");
        Assert.assertEquals("Не установилось свойство url", stand.getUrl(), "yandex.ru");
        Assert.assertEquals("Не установилось свойство count", stand.getCount(), 1);
        Assert.assertEquals("Не установилось свойство default", stand.getTestDefault(), "default");
        Assert.assertNull(stand.getTestNull());

    }

    @Test
    public void testMapToClass() {
    }
}