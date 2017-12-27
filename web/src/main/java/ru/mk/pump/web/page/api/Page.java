package ru.mk.pump.web.page.api;

import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.browsers.Browser;

public interface Page extends StrictInfo{

    Browser getBrowser();

    String getName();

    String getUrl();
}
