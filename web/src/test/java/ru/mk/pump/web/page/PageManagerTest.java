package ru.mk.pump.web.page;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.AbstractTestWithBrowser;
import ru.mk.pump.web.common.api.annotations.Alternative;
import ru.mk.pump.web.exceptions.ItemManagerException;

@SuppressWarnings("ConstantConditions")
@Slf4j
class PageManagerTest extends AbstractTestWithBrowser {

    private PageManager manager;

    @BeforeEach
    @Override
    public void setUp() {
        createBrowser();
        this.manager = new PageManager(getBrowsers(), AbstractPageTest.class.getPackage().getName());
    }

    @Test
    void getOne() {
        assertThat(manager.getOne("Главная страница")).isInstanceOfAny(AbstractPageTest.MainPage.class,AbstractPageTest.MainPageOther.class);
        assertThat(manager.getOne("MainPage")).isInstanceOf(AbstractPageTest.MainPage.class);

        assertThat(manager.getList("Регистрация")).hasOnlyElementsOfTypes(AbstractPageTest.RegPage.class);
        assertThat(manager.getList("RegPage")).hasOnlyElementsOfTypes(AbstractPageTest.RegPage.class);

        assertThatThrownBy(() -> manager.getOne("Главная страница_")).isInstanceOf(ItemManagerException.class);
    }

    @Test
    void emptyPackageOrNull() {
        this.manager = new PageManager(getBrowsers());
        assertThat(manager.getItemsSet()).isEmpty();
        assertThatThrownBy(() -> manager.getOne("Главная страница")).isInstanceOf(ItemManagerException.class);
        this.manager = new PageManager(getBrowsers());
        assertThat(manager.getItemsSet()).isEmpty();
        assertThatThrownBy(() -> manager.getOne("Главная страница")).isInstanceOf(ItemManagerException.class);
    }

    @Test
    void getList() {
        assertThat(manager.getList("Главная страница")).hasOnlyElementsOfTypes(AbstractPageTest.MainPageOther.class, AbstractPageTest.MainPage.class);
        log.info(Str.toPrettyString(manager.getCurrentList()));
        assertThat(manager.getList("Регистрация")).hasOnlyElementsOfTypes(AbstractPageTest.RegPage.class);
        log.info(Str.toPrettyString(manager.getCurrentList()));
    }

    @Test
    void find() {
        manager.addExtraFilter((m, c) -> c.isAnnotationPresent(Alternative.class));
        assertThat(manager.find("Главная страница", BasePage.class)).hasSize(1);

        manager.clearExtraFilter();
        assertThat(manager.find("Главная страница", BasePage.class)).hasSize(2);

    }

    @Test
    void getInfo() {
        assertThat(manager.getInfo()).containsOnlyKeys("browsers", "current list", "reporter", "current item", "type", "loaded items", "packages", "pageLoader");
    }

    @Test
    void testToString() {
        manager.getOne("Главная страница");
        manager.getList("Главная страница");
        assertThat(manager.toString()).isNotEmpty();
    }
}