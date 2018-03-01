package ru.mk.pump.web.page;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.exceptions.ElementStateException;

@Slf4j
class BasePageTest extends AbstractPageTest {

    @Test
    void testInitAllElements() {
        final RegPage page = new RegPage(getBrowser());
        page.open();

        assertThat(page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getInputs().get(0).type("MAX")).isEqualTo("MAX");
        assertThat(page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getInputs().get(1).type("MAX")).isEqualTo("MAX");
        assertThat(page.getMainForm().getRegFormZones().get(0).getRegFormZoneColumns().get(1).getInputs().get(2).type("MAX")).isEqualTo("MAX");

        InputDropDown inputDropDown = page.getMainForm().getRegFormZones().get(2).getRegFormZoneColumns().get(1).getInputDropDownRegions();
        assertThat(inputDropDown.getItems()).hasSize(20);
    }

    @Test
    void testAnnotationInit() {
        final RegPage page = new RegPage(getBrowser());
        page.open();

        assertThat(page.getPageTitle().info().getName()).isEqualTo("Заголовок");
        assertThat(page.getPageTitle().info().getDescription()).isEqualTo("Главный заголовок страницы");

        InputDropDown inputDropDown = page.getMainForm().getRegFormZones().get(2).getRegFormZoneColumns().get(1).getInputDropDownRegions();
        assertThat(((BaseElement) inputDropDown).getParams().size()).isEqualTo(5);
        assertThatCode(() -> inputDropDown.typeAndSelect("Москва")).doesNotThrowAnyException();

        assertThatThrownBy(
            () -> page.getMainForm().getRegFormZones().get(2).getRegFormZoneColumns().get(1).getInputDropDownRegionsFail().typeAndSelect("Москва"))
            .isInstanceOf(ElementStateException.class);
    }
}