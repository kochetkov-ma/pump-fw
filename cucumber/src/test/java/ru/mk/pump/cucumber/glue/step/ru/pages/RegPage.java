package ru.mk.pump.cucumber.glue.step.ru.pages;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.web.browsers.api.Browser;
import ru.mk.pump.web.common.api.annotations.*;
import ru.mk.pump.web.component.BaseComponent;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.api.concrete.TextArea;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.BasePage;
import ru.mk.pump.web.page.api.Page;

import java.util.List;

/**
 * Страница наследуется от {@link BasePage}.
 * Все приватные поля - это элементы либо компоненты
 */
@SuppressWarnings("WeakerAccess")
@PPage(value = "Регистрация",
        resource = "registration",
        desc = "Страница регистрации Цифровая Ипотека",
        baseUrl = "ipotekaonline.open.ru",
        extraUrls = {"ipotekaonline.open.ru/registration", "registration"}
)
public class RegPage extends BasePage {

    @FindBy(tagName = "h2")
    @PElement(value = "Заголовок", desc = "Главный заголовок страницы")
    @Getter
    private TextArea pageTitle;

    @PComponent("Форма")
    @FindBy(className = "mainlayout")
    @Getter
    private RegMainForm mainForm;

    @PElement("Не существующий")
    @FindBy(className = "mainlayout11")
    @Getter
    private ru.mk.pump.web.elements.api.Element notExists;

    public RegPage(Browser browser) {
        super(browser);
        getPageLoader().addExistsElements(pageTitle);
        getPageLoader().addDisplayedElements(pageTitle, mainForm);
    }

    public static class RegMainForm extends BaseComponent {

        @PComponent("Зона")
        @FindBy(xpath = "//div[@class='squished row form-group']")
        @Getter
        private List<RegFormZone> regFormZones;

        public RegMainForm(By avatarBy, Page page) {
            super(avatarBy, page);
        }
    }


    public static class RegFormZone extends BaseComponent {

        @PComponent("Колонка")
        @FindBy(xpath = "//div[contains(@class, 'column')]")
        @Getter
        private List<RegFormZoneColumn> regFormZoneColumns;

        public RegFormZone(By avatarBy, InternalElement parentElement) {
            super(avatarBy, parentElement);
        }
    }

    static class RegFormZoneColumn extends BaseComponent {

        @PElement("Поле")
        @FindBy(tagName = "input")
        @Getter
        private List<Input> inputs;

        @FindBy(className = "group-header")
        @Getter
        private TextArea header;

        @FindBy(id = "regionAutocompleteId")
        @PElement(value = "Регион", desc = "Выбор региона из выпадающего списка")
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

        @PAction("Метод")
        List<String> getStaticListOfString(String var, int number, boolean bool) {
            return ImmutableList.of("Строка_1", "Строка_2");
        }
    }
}