package ru.mk.pump.web.dx;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.AbstractTestWithBrowser;
import ru.mk.pump.web.dx.pages.Monitoring;

public class DxTest extends AbstractTestWithBrowser {

    private static final String APP = "И-170606-000566";

    @Test
    @Disabled
    void testFrame() {
        final Monitoring monitoringPage = new Monitoring(getBrowser());
        monitoringPage.open();

        monitoringPage.getHome().click();
        monitoringPage.getSales().click();
        monitoringPage.getApplications().click();
        monitoringPage.getMainFrame().getSearch().type(APP);
        monitoringPage.getMainFrame().getSearchStart().click();
        monitoringPage.getMainFrame().getFirstSearchResult().click();
    }
}
