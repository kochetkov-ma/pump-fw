package ru.mk.pump.web.page;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.internal.BaseElement;

@Slf4j
class BasePageTest extends AbstractPageTest {

    @Test
    void testInitAllElements() {
        final RegPage page = new RegPage(browser);
        page.initAllElements();
        page.open();

        assertThat(page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getInputs().get(0).type("MAX")).isEqualTo("MAX");
        assertThat(page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getInputs().get(1).type("MAX")).isEqualTo("MAX");
        assertThat(page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getInputs().get(2).type("MAX")).isEqualTo("MAX");

        InputDropDown inputDropDown = page.getMainForm().getRegFormZones().get(2).getRegFormZoneColumns().get(1).getInputDropDownRegions();
        assertThat(inputDropDown.getItems()).hasSize(20);
    }

    @Test
    void testAnnotationInit() {
        final RegPage page = new RegPage(browser);
        page.initAllElements();
        page.open();

        log.info(page.getPageTitle().getName());
        log.info(page.getPageTitle().getDescription());

        InputDropDown inputDropDown = page.getMainForm().getRegFormZones().get(2).getRegFormZoneColumns().get(1).getInputDropDownRegions();
        log.info(Strings.toPrettyString(((BaseElement) inputDropDown).getParams()));

    }
}