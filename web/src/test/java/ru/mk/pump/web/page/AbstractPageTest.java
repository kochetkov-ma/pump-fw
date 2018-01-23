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

    /**
     * Страница наследуется от {@link BasePage}.
     * Все приватные поля - это элементы либо компоненты
     */
    @SuppressWarnings("WeakerAccess")
    @Title(value = "Регистрация", desc = "Страница регистрации Цифровая Ипотека")
    static class RegPage extends BasePage {

        /**
         * Элемент TextArea.
         * Стандартная аннотация FindBy.
         * Аннотация {@link Title} определяет заголовок (имя) элемента и описание.
         */
        @FindBy(tagName = "h2")
        @Title(value = "Заголовок", desc = "Главный заголовок страницы")
        @Getter
        private TextArea pageTitle;

        /**
         * Элемент наследник BaseComponent. Это отдельный класс - кусок страницы, который объединяет несколько элементов.
         * Для него доступны все методы {@link Element}
         * Стандартная аннотация FindBy.
         * Аннотация {@link Title} определяет заголовок (имя) элемента (компонента) и описание.
         */
        @Title("Форма")
        @FindBy(className = "mainlayout")
        @Getter
        private RegMainForm mainForm;

        /**
         * Элемент общего интерфейса Element. Когда не важен конкретный класс элемента.
         * Стандартная аннотация FindBy.
         * Аннотация {@link Title} определяет заголовок (имя) элемента и описание.
         * Этот элемент не существует на реальной странице, но удачно будет инициализирован.
         * Ошибок не возникнет до взаимодействия.
         */
        @Title("Не существующий")
        @FindBy(className = "mainlayout11")
        @Getter
        private Element notExists;

        /**
         * Конcтруктор страницы. PUBLIC. Все конструкторы страниц, компонентов и элементов - PUBLIC!!!
         * @param browser Браузер
         */
        public RegPage(Browser browser) {
            super(browser);
            setName("Регистрация");
            setUrl(DMUrls.REG_PAGE_URL);
            getPageLoader().addExistsElements(pageTitle);
            getPageLoader().addDisplayedElements(pageTitle, mainForm);
        }

        /**
         * Класс компонента. Наследуется от {@link BaseComponent} - это часть страницы. Объекдиняет несколько элементов.
         * В данном случае это форма с множеством полей ввода и кнопок. Но описано только несколько.
         * Аннотация {@link Title} читается не из класса компонента, а из поля, в котором он объявлен на странице!
         * Не забываем про static для внутренних классов!
         */
        static class RegMainForm extends BaseComponent {

            /**
             * Список компонентов - это несколько зон в главной форме
             */
            @FindBy(xpath = "//div[@class='squished row form-group']")
            @Getter
            private List<RegFormZone> regFormZones;

            public RegMainForm(By avatarBy, Page page) {
                super(avatarBy, page);
            }
        }

        /**
         * Класс компонента. Наследуется от {@link BaseComponent} - это часть страницы. Объекдиняет несколько элементов.
         * В данном случае это одна из зон основной формы
         */
        static class RegFormZone extends BaseComponent {

            /**
             * Список компонентов - это несколько колонок в каждой зоне основной формы
             */
            @FindBy(xpath = "//div[contains(@class, 'column')]")
            @Getter
            private List<RegFormZoneColumn> regFormZoneColumns;

            public RegFormZone(By avatarBy, InternalElement parentElement) {
                super(avatarBy, parentElement);
            }
        }

        /**
         * Колонка в каждой из зон формы
         */
        static class RegFormZoneColumn extends BaseComponent {

            /**
             * Список элементов
             */
            @FindBy(tagName = "input")
            @Getter
            private List<Input> inputs;

            /**
             * Конечный элемент
             */
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
            /*аннотация для определяния нескольких СТРОКОВЫХ параметров элемента*/
            @PStrings({
                /*один СТРОКОВЫЙ параметр*/
                @PString(name = "testParam1", value = "paramValue1"),
                @PString(name = "testParam2", value = "paramValue2")
            })
            /*аннотация для определяния нескольких параметров элемента типа Локатор*/
            @PFindBys({
                /*один параметр Локатор*/
                @PFindBy(name = "extraBy", value = {@FindBy(xpath = "//div")}),
                @PFindBy(name = "iddInputBy", value = {@FindBy(tagName = "input")}),
                @PFindBy(name = "iddDropDownBy", value = {@FindBy(xpath = ".")})
            })
            @Getter
            private InputDropDown inputDropDownRegions;

            public RegFormZoneColumn(By avatarBy, InternalElement parentElement) {
                super(avatarBy, parentElement);
            }
        }
    }
}
