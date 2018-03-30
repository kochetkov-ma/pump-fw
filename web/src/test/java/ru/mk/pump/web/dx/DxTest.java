package ru.mk.pump.web.dx;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.AbstractTestWithBrowser;
import ru.mk.pump.web.dx.pages.Monitoring;

public class DxTest extends AbstractTestWithBrowser {

    @Test
    @Disabled
    void testFrame() {
        final Monitoring page = new Monitoring(getBrowser());
        page.open();
    }
}
