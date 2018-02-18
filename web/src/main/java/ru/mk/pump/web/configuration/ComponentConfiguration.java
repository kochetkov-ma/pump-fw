package ru.mk.pump.web.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.config.Property;

@SuppressWarnings("WeakerAccess")
@Data
@NoArgsConstructor
public class ComponentConfiguration {

    @Property(value = "component.packages", required = false)
    private String[] packages;
}
