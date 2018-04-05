package ru.mk.pump.web.utils;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ImmutableCapabilities;
import ru.mk.pump.commons.exception.UtilException;
import ru.mk.pump.commons.utils.ProjectResources;

@Slf4j
class CapabilitiesUtilsTest {

    @Test
    void loadFromProperties() {
        Capabilities expected = new ImmutableCapabilities("test_caps1", "test_value1", "test_caps2", "test_value2");
        String pathString = ProjectResources.findResource("caps.properties").toString();

        assertThat(CapabilitiesUtils.loadFromProperties(pathString)).isEqualTo(expected);
        assertThat(CapabilitiesUtils.loadFromProperties("caps.properties")).isEqualTo(expected);

        assertThatThrownBy(() -> CapabilitiesUtils.loadFromProperties("not exists")).isInstanceOf(UtilException.class);
        assertThatThrownBy(() -> CapabilitiesUtils.loadFromProperties(pathString + " - not exists")).isInstanceOf(UtilException.class);
    }
}