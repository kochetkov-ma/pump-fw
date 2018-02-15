package ru.mk.pump.web.common;

import static org.assertj.core.api.Assertions.*;

import com.google.common.collect.ImmutableList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.exception.ExecutionException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.exception.UtilException;
import ru.mk.pump.web.AbstractTestWithBrowser;
import ru.mk.pump.web.component.ComponentList;
import ru.mk.pump.web.component.ComponentStaticManager;
import ru.mk.pump.web.component.ComponentStaticManagerTest;
import ru.mk.pump.web.component.ComponentStaticManagerTest.OneComponent;
import ru.mk.pump.web.component.ComponentStaticManagerTest.OneComponent.InnerComponent;
import ru.mk.pump.web.component.ComponentStaticManagerTest.OneComponent.InnerOtherComponent;
import ru.mk.pump.web.elements.ElementList;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.exceptions.ItemManagerException;
import ru.mk.pump.web.page.AbstractPageTest;
import ru.mk.pump.web.page.AbstractPageTest.MainPage;
import ru.mk.pump.web.page.AbstractPageTest.MainPageOther;
import ru.mk.pump.web.page.AbstractPageTest.RegPage;
import ru.mk.pump.web.page.AbstractPageTest.RegPage.RegFormZone;
import ru.mk.pump.web.page.PageManager;
import ru.mk.pump.web.utils.TestVars;

@SuppressWarnings("ConstantConditions")
@Slf4j
class WebItemsControllerTest extends AbstractTestWithBrowser {

    private WebItemsController controller;

    @BeforeEach
    @Override
    public void setUp() {
        createBrowser();
        ComponentStaticManager cManager = new ComponentStaticManager(getBrowser(), ComponentStaticManagerTest.class.getPackage().getName());
        PageManager pManager = new PageManager(getBrowser(), AbstractPageTest.class.getPackage().getName());
        this.controller = new WebItemsController(pManager, cManager, TestVars.of("test_var", "value"));

    }

    @Test
    void getInfo() {
        controller.execute("OneComponent.method(SIMPLE)");
        assertThat(PumpMessage.of(controller).toPrettyString()).contains("type", "page manager", "component manager", "last expression", "lastResult");
    }

    @Test
    void initComponent() {
        assertThat(controller.initComponent("AInnerComponent")).isInstanceOfAny(InnerComponent.class, InnerOtherComponent.class);
        assertThat((controller.initComponent("InnerComponent"))).isInstanceOf(InnerComponent.class);
        assertThat((controller.initComponent("InnerOtherComponent"))).isInstanceOf(InnerOtherComponent.class);
        assertThat((controller.initComponent("AOneComponent"))).isInstanceOf(OneComponent.class);
        assertThat((controller.initComponent("OneComponent"))).isInstanceOf(OneComponent.class);

        assertThatThrownBy(() -> controller.initComponent("")).isInstanceOf(ItemManagerException.class);
        assertThatThrownBy(() -> controller.initComponent(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> controller.initComponent("not_exists")).isInstanceOf(ItemManagerException.class);
    }

    @Test
    void initPage() {
        assertThat(controller.initPage("Главная страница")).isInstanceOfAny(MainPageOther.class, MainPage.class);
        assertThat(controller.initPage("MainPageOther")).isInstanceOfAny(MainPageOther.class);
        assertThat(controller.initPage("MainPage")).isInstanceOfAny(MainPage.class);
        assertThat(controller.initPage("Регистрация")).isInstanceOfAny(RegPage.class);

        assertThatThrownBy(() -> controller.initPage("")).isInstanceOfAny(ItemManagerException.class);
        assertThatThrownBy(() -> controller.initPage(null)).isInstanceOfAny(NullPointerException.class);
        assertThatThrownBy(() -> controller.initPage("RegMainForm")).isInstanceOfAny(ItemManagerException.class);

    }

    @Test
    void executeMethod() {
        assertThatThrownBy(() -> controller.execute("OneComponent.methodList(1 2 3 4)")).isInstanceOf(ExecutionException.class)
            .hasCauseInstanceOf(UtilException.class);
        assertThatThrownBy(() -> controller.execute("OneComponent.notExists()")).isInstanceOf(ExecutionException.class);
        assertThatThrownBy(() -> controller.execute("OneComponent.methodString()")).isInstanceOf(ExecutionException.class);

        assertThat(controller.execute("OneComponent.method(1,2,true)"))
            .isInstanceOf(List.class)
            .isEqualTo(ImmutableList.<Object>of(1L, 2, true));
        assertThat(controller.execute("OneComponent.method(NO_BEFORE)")).isEqualTo(ActionStrategy.NO_BEFORE);
        assertThat(controller.execute("OneComponent.method(SIMPLE)")).isEqualTo(ActionStrategy.SIMPLE);

    }

    @Test
    void execute() {
        assertThat(controller.execute("$groovy{new Date()}")).isInstanceOf(Date.class);
        assertThat(controller.execute("${no}")).isNull();
        assertThat(controller.execute("AOneComponent.element_one"))
            .isNotNull()
            .isInstanceOf(Input.class);

        assertThat(controller.execute("Регистрация.Форма.Зона"))
            .isNotNull()
            .isInstanceOf(ComponentList.class);

        assertThat(controller.execute("Регистрация.Форма.Зона[0]"))
            .isNotNull()
            .isInstanceOf(RegFormZone.class);

        assertThat(controller.execute("Регистрация.Форма.Зона[0].Колонка[0].Поле[0]"))
            .isNotNull()
            .isInstanceOf(Input.class);

        assertThat(controller.execute("Регистрация.Форма.Зона[0].Колонка[0].Поле"))
            .isNotNull()
            .isInstanceOf(ElementList.class);

        assertThat(controller.execute("Регистрация.Форма.Зона[0].Колонка[0].Метод(Строка,1,true)"))
            .isEqualTo(ImmutableList.of("Строка_1", "Строка_2"));

        log.info(PumpMessage.of(controller).toPrettyString());
    }

    @Test
    void executeRelative() {
        assertThat(controller.execute(new RegPage(getBrowser()), "$groovy{new Date()}")).isInstanceOf(Date.class);
        assertThat(controller.execute(new RegPage(getBrowser()), "${no}")).isNull();
        assertThat(controller.execute("AOneComponent.element_one"))
            .isNotNull()
            .isInstanceOf(Input.class);

        assertThat(controller.execute(new RegPage(getBrowser()), "Форма.Зона"))
            .isNotNull()
            .isInstanceOf(ComponentList.class);

        assertThat(controller.execute(new RegPage(getBrowser()), "Форма.Зона[0]"))
            .isNotNull()
            .isInstanceOf(RegFormZone.class);

        assertThat(controller.execute(new RegPage(getBrowser()), "Форма.Зона[0].Колонка[0].Поле[0]"))
            .isNotNull()
            .isInstanceOf(Input.class);

        assertThat(controller.execute(new RegPage(getBrowser()), "Форма.Зона[0].Колонка[0].Поле"))
            .isNotNull()
            .isInstanceOf(ElementList.class);

        assertThat(controller.execute(new RegPage(getBrowser()), "Форма.Зона[0].Колонка[0].Метод(Строка,1,true)"))
            .isEqualTo(ImmutableList.of("Строка_1", "Строка_2"));

        log.info(PumpMessage.of(controller).toPrettyString());
    }

    @Test
    void cast() {
    }

    @Test
    void getPageManager() {
        assertThat(controller.getPageManager()).isNotNull();
    }

    @Test
    void getComponentManager() {
        assertThat(controller.getComponentManager()).isNotNull();
    }


    @Test
    void executeOnCurrentPage() {
        assertThatThrownBy(() -> controller.executeOnCurrentPage("any")).isInstanceOf(ExecutionException.class);
        controller.initPage("Регистрация");

        assertThat(controller.executeOnCurrentPage("$groovy{new Date()}")).isInstanceOf(Date.class);
        assertThat(controller.executeOnCurrentPage("${no}")).isNull();

        assertThat(controller.executeOnCurrentPage("Форма.Зона"))
            .isNotNull()
            .isInstanceOf(ComponentList.class);

        assertThat(controller.executeOnCurrentPage("Форма.Зона[0]"))
            .isNotNull()
            .isInstanceOf(RegFormZone.class);

        assertThat(controller.executeOnCurrentPage("Форма.Зона[0].Колонка[0].Поле[0]"))
            .isNotNull()
            .isInstanceOf(Input.class);

        assertThat(controller.executeOnCurrentPage("Форма.Зона[0].Колонка[0].Поле"))
            .isNotNull()
            .isInstanceOf(ElementList.class);

        assertThat(controller.executeOnCurrentPage("Форма.Зона[0].Колонка[0].Метод(Строка,1,true)"))
            .isEqualTo(ImmutableList.of("Строка_1", "Строка_2"));
    }

    @Test
    void executeOnCurrentComponent() {

    }
}