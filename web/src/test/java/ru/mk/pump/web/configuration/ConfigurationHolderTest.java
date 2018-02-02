package ru.mk.pump.web.configuration;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.config.ConfigurationsLoader;
import ru.mk.pump.commons.utils.EnvVariables;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.web.exceptions.ConfigurationException;

@Slf4j
class ConfigurationHolderTest {

    private static Configuration instance1;

    private static Configuration instance2;

    private static ConfigurationsLoader loader1;

    private static ConfigurationsLoader loader2;

    @Test
    void get() throws InterruptedException {
        ConfigurationHolder.cleanup();
        Thread thread1 = new Thread(() -> ConfigurationHolderTest.instance1 = ConfigurationHolder.get());
        Thread thread2 = new Thread(() -> ConfigurationHolderTest.instance2 = ConfigurationHolder.get());
        thread1.run();
        thread2.run();
        thread1.join();
        thread2.join();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void init() {
        assertThatThrownBy(() -> ConfigurationHolder.init("not.exists.properties"))
            .isInstanceOf(IllegalArgumentException.class);

        ConfigurationHolder.init("pump.properties");
        assertThat(ConfigurationHolder.get().getApplicationName()).isEqualTo("testing application");
        assertThat(ConfigurationHolder.get().getElement().getWindowWidthOffset()).isEqualTo(0);
    }

    @Test
    void init1() {
        Configuration conf = new Configuration();
        conf.setApplicationName("test");
        conf.getElement().setWindowWidthOffset(10);
        ConfigurationHolder.init(conf);
        assertThat(ConfigurationHolder.get().getApplicationName()).isEqualTo("test");
        assertThat(ConfigurationHolder.get().getElement().getWindowWidthOffset()).isEqualTo(10);
    }

    @Test
    void init2() {
        ConfigurationHolder.init(ProjectResources.findResource("pump.properties"));
        assertThat(ConfigurationHolder.get().getApplicationName()).isEqualTo("testing application");
        assertThat(ConfigurationHolder.get().getElement().getWindowWidthOffset()).isEqualTo(0);
        assertThatThrownBy(() -> ConfigurationHolder.init(Paths.get("C:no")))
            .isInstanceOf(ConfigurationException.class);
        assertThatThrownBy(() -> ConfigurationHolder.init(Paths.get("C:/no")))
            .isInstanceOf(ConfigurationException.class);
    }

    @Test
    void init3() throws IOException {
        ConfigurationHolder.init(FileUtils.openInputStream(ProjectResources.findResource("pump.properties").toFile()));
        assertThat(ConfigurationHolder.get().getApplicationName()).isEqualTo("testing application");
        assertThat(ConfigurationHolder.get().getElement().getWindowWidthOffset()).isEqualTo(0);
    }

    @Test
    void init4() {
        ConfigurationHolder.init(new ConfigurationsLoader(ProjectResources.findResource("pump.properties")));
        assertThat(ConfigurationHolder.get().getApplicationName()).isEqualTo("testing application");
        assertThat(ConfigurationHolder.get().getElement().getWindowWidthOffset()).isEqualTo(0);
    }

    @Test
    void initSystemEnv() {
        assertThatThrownBy(() -> ConfigurationHolder.initSystemEnv("not.exists"))
            .isInstanceOf(ConfigurationException.class);
        System.setProperty("pump.configuration.path", "pump.properties");
        EnvVariables.reloadCache();
        ConfigurationHolder.initSystemEnv("pump.configuration.path");
        assertThat(ConfigurationHolder.get().getApplicationName()).isEqualTo("testing application");
        assertThat(ConfigurationHolder.get().getElement().getWindowWidthOffset()).isEqualTo(0);
    }

    @Test
    void initDefault() {
        ConfigurationHolder.initDefault();
        assertThat(ConfigurationHolder.get().getApplicationName()).isEqualTo("testing application");
        assertThat(ConfigurationHolder.get().getElement().getWindowWidthOffset()).isEqualTo(0);

        System.setProperty("pump.configuration.path", "not.exists.properties");
        EnvVariables.reloadCache();
        ConfigurationHolder.initDefault();
        assertThat(ConfigurationHolder.get().getApplicationName()).isEqualTo("undefined");
        assertThat(ConfigurationHolder.get().getElement().getWindowWidthOffset()).isEqualTo(0);
    }

    @AfterEach
    void clear(){
        System.clearProperty("pump.configuration.path");
        EnvVariables.reloadCache();
    }

    @Test
    void getLoader() throws InterruptedException {
        ConfigurationHolder.cleanup();
        Thread thread1 = new Thread(() -> {
            ConfigurationHolder.initDefault();
            loader1 = ConfigurationHolder.getLoader();
        });
        Thread thread2 = new Thread(() -> {
            ConfigurationHolder.initDefault();
            loader2 = ConfigurationHolder.getLoader();
        });
        thread1.run();
        thread2.run();
        thread1.join();
        thread2.join();
        assertThat(loader1).isNotEqualTo(loader2);
        log.info(loader1.getHistory().toString());
        log.info(loader2.getHistory().toString());
        assertThat(loader1.getHistory().size()).isEqualTo(1);
        assertThat(loader2.getHistory().size()).isEqualTo(1);
    }
}