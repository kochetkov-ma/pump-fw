package ru.mk.pump.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.utils.History.Info;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.commons.utils.Str;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ConfigurationsLoaderTest {

    @Test
    void testLoad() {
        final ConfigurationsLoader configurationsLoader = new ConfigurationsLoader(new ProjectResources(getClass()).findResource("stand.properties"), true);
        configurationsLoader.load();
    }

    @Test
    void testGetMap() {
        final ConfigurationsLoader configurationsLoader = new ConfigurationsLoader(new ProjectResources(getClass()).findResource("stand.properties"), true);
        assertThat(configurationsLoader.getMap()).isEmpty();
        configurationsLoader.load();
        log.debug(Str.toPrettyString(configurationsLoader.getMap()));
        assertThat(configurationsLoader.getMap()).hasSize(10)
                .containsOnlyKeys("main.reporting", "two.one", "main.url", "common", "second.count", "second.url", "main.count", "two.two", "second.reporting",
                        "enum");

    }

    @Test
    void testMapToObject() {
        /*to test load from ENV or SYSTEM VARS*/
        System.setProperty("common.extra", "from_env");
        final ConfigurationsLoader configurationsLoader = new ConfigurationsLoader(new ProjectResources(getClass()).findResource("stand.properties"), true);
        configurationsLoader.load();
        /*create nonArg object*/
        final Stand stand = new Stand();
        /*rewrite object*/
        configurationsLoader.toObject(stand, "main");
        assertThat(stand.getEnumProperty()).isEqualTo(En.ONE);
        assertThat(stand.getCommon()).isEqualTo("common");
        assertThat(stand.getExtra()).isEqualTo("from_env");
        assertThat(stand.getUrl()).isEqualTo("yandex.ru");
        assertThat(stand.getCount()).isEqualTo(1);
        assertThat(stand.getTestDefault()).isEqualTo("default");
        assertThat(stand.getTestNull()).isNull();

    }

    @Test
    void testMapToClass() {
        /*expected*/
        final Stand one = new Stand();
        one.setCommon("commonStand");
        one.setCount(1);
        one.setExtra("from_env");
        one.setUrl("yandex.ru");
        one.setTestDefault("default");
        one.setReporting(true);
        one.setTwo(new Two("one", "one"));

        final Stand two = new Stand();
        two.setCommon("commonStandA");
        two.setCount(2);
        two.setExtra("from_env");
        two.setUrl("google.ru");
        two.setTestDefault("default");
        two.setReporting(false);
        two.setTwo(new Two("two", "two"));

        /*to test load from ENV or SYSTEM VARS*/
        System.setProperty("common.extra", "from_env");
        ConfigurationsLoader configurationsLoader = new ConfigurationsLoader(new ProjectResources(getClass()).findResource("stands.properties"), true);
        configurationsLoader.load();
        /*create new object from class*/
        final Stands stand = configurationsLoader.toObject(Stands.class);
        assertThat(stand.getName()).isEqualTo("standsName");
        assertThat(stand.isComplex()).isEqualTo(true);
        assertThat(stand.getStandOne()).isEqualTo(one);
        assertThat(stand.getStandTwo()).isEqualTo(two);
    }

    @Test
    void testHistory() {
        System.setProperty("common.extra", "from_env");
        final ConfigurationsLoader configurationsLoader = new ConfigurationsLoader(new ProjectResources(getClass()).findResource("stands.properties"), true);
        configurationsLoader.load();
        log.debug(Str.toPrettyString(configurationsLoader.getHistory().asList()));
        final Stands stand = configurationsLoader.toObject(Stands.class);
        configurationsLoader.toObject(Stands.class);
        configurationsLoader.toObject(Stands.class);
        log.debug(Str.toPrettyString(configurationsLoader.getHistory().asList()));
        final Optional<Info<String>> lst = configurationsLoader.getHistory().findLastById(ConfigurationsLoader.getHistoryId(Stands.class, "env"));
        log.debug(lst.orElse(Info.of("undefined")).getPayload());
    }
}