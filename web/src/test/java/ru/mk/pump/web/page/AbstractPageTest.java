package ru.mk.pump.web.page;

import java.util.List;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.AbstractTestWithBrowser;
import ru.mk.pump.web.DMUrls;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PFindBy;
import ru.mk.pump.web.common.api.annotations.PFindBys;
import ru.mk.pump.web.common.api.annotations.PString;
import ru.mk.pump.web.common.api.annotations.PStrings;
import ru.mk.pump.web.common.api.annotations.Title;
import ru.mk.pump.web.component.BaseComponent;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.api.concrete.TextArea;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings("ALL")
abstract class AbstractPageTest extends AbstractTestWithBrowser {

    @SuppressWarnings("WeakerAccess")
    @Title("Регистрация")
    public static class RegPage extends BasePage {

        @FindBy(tagName = "h2")
        @Title(value = "Заголовок", desc = "Главный заголовок страницы")
        @Getter
        private TextArea pageTitle;

        @FindBy(className = "mainlayout")
        @Getter
        private RegMainForm mainForm;

        @Title("Не существующий")
        @FindBy(className = "mainlayout11")
        @Getter
        private Element notExists;

        public RegPage(Browser browser) {
            super(browser);
            setName("Регистрация");
            setUrl(DMUrls.REG_PAGE_URL);
            getPageLoader().addExistsElements(pageTitle);
            getPageLoader().addDisplayedElements(pageTitle, mainForm);
        }
    }

    public static class RegMainForm extends BaseComponent {

        @FindBy(xpath = "//div[@class='squished row form-group']")
        @Getter
        private List<RegFormZone> regFormZones;

        public RegMainForm(By avatarBy, Page page) {
            super(avatarBy, page);
        }
    }

    public static class RegFormZone extends BaseComponent {

        @FindBy(xpath = "//div[contains(@class, 'column')]")
        @Getter
        private List<RegFormZoneColumn> regFormZoneColumns;

        public RegFormZone(By avatarBy, InternalElement parentElement) {
            super(avatarBy, parentElement);
        }
    }

    public static class RegFormZoneColumn extends BaseComponent {

        @FindBy(tagName = "input")
        @Getter
        private List<Input> inputs;

        @FindBy(className = "group-header")
        @Getter
        private TextArea header;

        @FindBy(id = "regionAutocompleteId")
        @Title(value = "Регион", desc = "Выбор региона из выпадающего списка")
        @PStrings({
            @PString(name = "testParam1", value = "paramValue1"),
            @PString(name = "testParam2", value = "paramValue2")
        })
        @PFindBys({
            @PFindBy(name = "extraBy", value = {@FindBy(xpath = "//div")}),
            @PFindBy(name = "iddInputBy", value = {@FindBy(tagName = "input")}),
            @PFindBy(name = "iddLoadBy", value = {@FindBy(tagName = "input")}),
            @PFindBy(name = "iddDropDownBy", value = {@FindBy(xpath = ".")})
        })
        @Getter
        private InputDropDown inputDropDownRegionsFail;

        @FindBy(id = "regionAutocompleteId")
        @Title(value = "Регион", desc = "Выбор региона из выпадающего списка")
        @PStrings({
            @PString(name = "testParam1", value = "paramValue1"),
            @PString(name = "testParam2", value = "paramValue2")
        })
        @PFindBys({
            @PFindBy(name = "extraBy", value = {@FindBy(xpath = "//div")}),
            @PFindBy(name = "iddInputBy", value = {@FindBy(tagName = "input")}),
            //@PFindBy(name = "iddLoadBy", value = {@FindBy(tagName = "input")}),
            @PFindBy(name = "iddDropDownBy", value = {@FindBy(xpath = ".")})
        })
        @Getter
        private InputDropDown inputDropDownRegions;

        public RegFormZoneColumn(By avatarBy, InternalElement parentElement) {
            super(avatarBy, parentElement);
        }
    }
}
