package ru.mk.pump.web.page.api;

import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.component.api.Component;

public interface Page extends StrictInfo, Component{

    void open();

    Browser getBrowser();

    String getName();

    String getUrl();
}
