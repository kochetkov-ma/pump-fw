package ru.mk.pump.web.page;

import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.commons.utils.ProjectResources;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.browsers.configuration.BrowserConfig;
import ru.mk.pump.web.browsers.configuration.BrowserType;
import ru.mk.pump.web.browsers.configuration.Size;
import ru.mk.pump.web.common.api.annotations.PFindBy;
import ru.mk.pump.web.common.api.annotations.PFindBys;
import ru.mk.pump.web.common.api.annotations.PString;
import ru.mk.pump.web.common.api.annotations.PStrings;
import ru.mk.pump.web.common.api.annotations.Title;
import ru.mk.pump.web.component.BaseComponent;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.api.concrete.TextArea;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.internal.ElementWaiter;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractPageTest {

    //private static final BrowserType BROWSER_TYPE = BrowserType.valueOf(EnvVariables.get("BROWSER", "PHANTOMJS"));
    private static final BrowserType BROWSER_TYPE = BrowserType.CHROME;

    public static Browsers browsers;

    public Browser browser;

    public static BrowserConfig config;

    @AfterAll
    public static void afterAll() {
        browsers.close();
    }

    @BeforeAll
    public static void beforeAll() {
        config = new BrowserConfig(false, Size.of(true), BROWSER_TYPE);
        if (BROWSER_TYPE != BrowserType.PHANTOMJS) {
            config.setWebDriverPath(ProjectResources.findResource("chromedriver.exe").toString());
        }
        browsers = new Browsers();
    }

    @BeforeEach
    public void setUp() {
        if (!browsers.has()) {
            browser = createBrowser();
            browser.start();
        } else {
            browser = browsers.get();
        }
    }

    @AfterEach
    public void tearDown() {
        ElementWaiter.DEFAULT_TIMEOUT_S = 10;
    }


    @SuppressWarnings("WeakerAccess")
    @Title("Регистрация")
    public static class RegPage extends BasePage {

        @FindBy(tagName = "h1")
        @Title(value = "Заголовок", desc = "Главный заголовок страницы")
        @Getter
        private TextArea pageTitle;

        @FindBy(className = "mainlayout")
        @Getter
        private RegMainForm mainForm;

        public RegPage(Browser browser) {
            super(browser);
            setName("Регистрация");
            setUrl("https://app-digitalmortgage003.open.ru/registration");

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


    protected Browser createBrowser() {
        return browsers.newBrowser(config);
    }

}
