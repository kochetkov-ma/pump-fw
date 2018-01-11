package ru.mk.pump.web.page;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class BasePageTest extends AbstractPageTest {

    @Test
    void initAllElements() {
        final RegPage page = new RegPage(browser);
        page.initAllElements();
        page.open();
/*
        log.info(page.getPageTitle().getText());
        log.info(page.getMainForm().getText());

        log.info(page.getMainForm().getRegFormZones().get(1).getText());
        log.info(page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getText());
*/

        log.info(String.valueOf(page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getInputs().get(0).type("MAX")));
        log.info(String.valueOf(page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getInputs().get(1).type("MAX")));
        log.info(String.valueOf(page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getInputs().get(2).type("MAX")));
        log.info("[CHECKPOINT] - START");
        page.getMainForm().getRegFormZones().get(2).getRegFormZoneColumns().get(1).getInputDropDownRegions().typeAndSelect("Москва");
        log.info("[CHECKPOINT] - STOP");
        log.info(page.getMainForm().getRegFormZones().get(2).getRegFormZoneColumns().get(1).getInputDropDownRegions().getText());

    }
}