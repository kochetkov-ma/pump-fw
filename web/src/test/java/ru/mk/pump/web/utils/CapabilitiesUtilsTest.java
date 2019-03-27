package ru.mk.pump.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ImmutableCapabilities;
import ru.mk.pump.commons.exception.UtilException;
import ru.mk.pump.commons.utils.ProjectResources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class CapabilitiesUtilsTest {

    @Test
    void loadFromProperties() {
        Capabilities expected = new ImmutableCapabilities("test_caps1", "test_value1", "test_caps2", "test_value2");
        String pathString = new ProjectResources(getClass()).findResource("caps.properties").toString();

        assertThat(CapabilitiesUtils.loadFromProperties(pathString, new ProjectResources(getClass()))).isEqualTo(expected);
        assertThat(CapabilitiesUtils.loadFromProperties("caps.properties", new ProjectResources(getClass()))).isEqualTo(expected);

        assertThatThrownBy(() -> CapabilitiesUtils.loadFromProperties("not exists", new ProjectResources(getClass()))).isInstanceOf(UtilException.class);
        assertThatThrownBy(() -> CapabilitiesUtils.loadFromProperties(pathString + " - not exists", new ProjectResources(getClass())))
                .isInstanceOf(UtilException.class);
    }
}