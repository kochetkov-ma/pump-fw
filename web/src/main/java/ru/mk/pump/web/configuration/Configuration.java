package ru.mk.pump.web.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.mk.pump.commons.config.Property;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Configuration {

    @Property("application.name")
    private String applicationName;

    @Property("browser")
    private BrowserConfig browserConfig;

    @Property("reporting")
    private ReportingConfiguration reporting = new ReportingConfiguration();

    @Property("verify")
    private VerifyConfiguration verify = new VerifyConfiguration();

    @Property("element")
    private ElementConfiguration element = new ElementConfiguration();

    @Property("page")
    private PageConfiguration page = new PageConfiguration();

    @Property("component")
    private ComponentConfiguration component = new ComponentConfiguration();
}
