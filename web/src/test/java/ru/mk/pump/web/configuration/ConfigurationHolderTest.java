package ru.mk.pump.web.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.io.Resources;
import java.io.InputStream;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.*;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.utils.EnvVariables;

@Slf4j
class ConfigurationHolderTest {



    @Test
    void get() {


    }

    @Test
    void init() {
    }

    @Test
    void init1() {
    }

    @Test
    void init2() {
    }

    @Test
    void init3() {
    }

    @Test
    void init4() {
    }

    @Test
    void initSystemEnv() {
    }

    @Test
    void initDefault() {
        ConfigurationHolder.initDefault();
        assertThat(ConfigurationHolder.get().getApplicationName()).isEqualTo("testing application");
        assertThat(ConfigurationHolder.get().getWindowSizeOffset()).isEqualTo(0);

        System.setProperty("pump.configuration.path","not.exists.properties");
        EnvVariables.reloadCache();
        ConfigurationHolder.initDefault();
        assertThat(ConfigurationHolder.get().getApplicationName()).isEqualTo("undefined");
        assertThat(ConfigurationHolder.get().getWindowSizeOffset()).isEqualTo(0);

    }

    @Test
    void getLoader() {
    }
}