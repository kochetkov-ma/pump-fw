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

        page.getHome().click();
        page.getSales().click();
        page.getApplications().click();
        page.getHome().click();

        page.getMainFrame().getFrame().getFrame().getFrame().getSearchInput().type("Тест");

        page.getHome().click();
        page.getSales().click();
        page.getApplications().click();
        page.getHome().click();

        page.getMainFrame().getFrame().getFrame().getFrame().getSearchInput().type("Тест");
    }
}
