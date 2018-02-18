package ru.mk.pump.web.component;

import static org.assertj.core.api.Assertions.*;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.AbstractTestWithBrowser;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.annotations.PComponent;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.enums.ActionStrategy;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ItemManagerException;

@SuppressWarnings({"unchecked", "unused"})
@Slf4j
public class ComponentStaticManagerTest extends AbstractTestWithBrowser {

    private ComponentStaticManager manager;

    @BeforeEach
    @Override
    public void setUp() {
        createBrowser();
        this.manager = new ComponentStaticManager(getBrowsers(), getClass().getPackage().getName());
    }


    @PComponent(value = "AOneComponent", desc = "Annotation description")
    @FindBy(id = "component_one")
    public static class OneComponent extends BaseComponent {

        @PElement("element_one")
        @FindBy(id = "element_one")
        @Getter
        private Input elementOne;

        @FindBy(id = "element_two")
        @Getter
        private Element elemntTwo;

        @FindBy(id = "inner")
        @Getter
        private InnerComponent inner;

        @PComponent("AInnerComponent")
        @FindBy(id = "component_inner")
        public static class InnerComponent extends BaseComponent {

            @FindBy(id = "element_two")
            @Getter
            private Element innerElementTwo;

            public InnerComponent(By avatarBy, InternalElement parentElement) {
                super(avatarBy, parentElement);
            }

            public InnerComponent(By avatarBy, Browser browser) {
                super(avatarBy, browser);
            }
        }

        public String methodString(String value) {
            return value;
        }

        public void method() {

        }

        public List<String> methodList(List<String> list) {
            return list;
        }

        public List<Object> method(long numberLong, int numberInt, boolean bol) {
            return ImmutableList.of(numberLong, numberInt, bol);
        }

        public ActionStrategy method(ActionStrategy actionStrategy) {
            return actionStrategy;
        }

        @PComponent("AInnerComponent")
        @FindBy(id = "component_inner")
        public static class InnerOtherComponent extends BaseComponent {

            @FindBy(id = "element_two")
            @Getter
            private Element innerElementTwo;

            public InnerOtherComponent(By avatarBy, InternalElement parentElement) {
                super(avatarBy, parentElement);
            }

            public InnerOtherComponent(By avatarBy, Browser browser) {
                super(avatarBy, browser);
            }
        }

        public OneComponent(By avatarBy, Browser browser) {
            super(avatarBy, browser);
        }
    }

    @Test
    void getInfo() {
        manager.getOne("AInnerComponent");
        manager.getList("AInnerComponent");
        assertThat(PumpMessage.of(manager).toPrettyString())
            .contains("browser", "current list", "reporter", "current item", "type", "loaded items", "packages");
    }

    @Test
    void testToString() {
        manager.getOne("AInnerComponent");
        manager.getList("AInnerComponent");
        assertThat(manager.toString()).isNotEmpty();
    }

    @Test
    void getOne() {
        BaseComponent result;
        result = manager.getOne("AInnerComponent");
        log.info(Strings.toString(result));
        assertThat(result).isInstanceOfAny(OneComponent.InnerComponent.class, OneComponent.InnerOtherComponent.class);

        result = manager.getOne("InnerComponent");
        log.info(Strings.toString(result));
        assertThat(result).isInstanceOf(OneComponent.InnerComponent.class);

        result = manager.getOne("InnerOtherComponent");
        log.info(Strings.toString(result));
        assertThat(result).isInstanceOf(OneComponent.InnerOtherComponent.class);

        result = manager.getOne("AOneComponent");
        log.info(Strings.toString(result));
        assertThat(result).isInstanceOf(OneComponent.class);

        result = manager.getOne("OneComponent");
        log.info(Strings.toString(result));
        assertThat(result).isInstanceOf(OneComponent.class);

        assertThatThrownBy(() -> manager.getOne("")).isInstanceOf(ItemManagerException.class);
    }

    @Test
    void getList() {
        List<BaseComponent> result;
        result = manager.getList("AInnerComponent");
        log.info(Strings.toString(result));
        assertThat(result)
            .hasSize(2)
            .hasOnlyElementsOfTypes(OneComponent.InnerComponent.class, OneComponent.InnerOtherComponent.class);

        result = manager.getList("InnerComponent");
        log.info(Strings.toString(result));
        assertThat(result)
            .hasSize(1)
            .hasOnlyElementsOfTypes(OneComponent.InnerComponent.class);

        result = manager.getList("InnerOtherComponent");
        log.info(Strings.toString(result));
        assertThat(result)
            .hasSize(1)
            .hasOnlyElementsOfTypes(OneComponent.InnerOtherComponent.class);

        result = manager.getList("AOneComponent");
        log.info(Strings.toString(result));
        assertThat(result)
            .hasSize(1)
            .hasOnlyElementsOfTypes(OneComponent.class);

        result = manager.getList("OneComponent");
        log.info(Strings.toString(result));
        assertThat(result)
            .hasSize(1)
            .hasOnlyElementsOfTypes(OneComponent.class);

        result = manager.getList("");
        log.info(Strings.toString(result));
        assertThat(result)
            .isEmpty();
    }



    @Test
    void find() {
        Set<Class<OneComponent>> result;
        result = manager.find("OneComponent", OneComponent.class);
        assertThat(result).hasSize(1);

        result = manager.find("AOneComponent", OneComponent.class);
        log.info(Strings.toString(result));
        assertThat(result).hasSize(1);

        Set<Class<OneComponent.InnerComponent>> resultInner;
        resultInner = manager.find("InnerComponent", OneComponent.InnerComponent.class);
        log.info(Strings.toString(resultInner));
        assertThat(result).hasSize(1);

        resultInner = manager.find("AInnerComponent", OneComponent.InnerComponent.class);
        log.info(Strings.toString(resultInner));
        assertThat(result).hasSize(1);

        assertThat(manager.find("OneComponent_not_exists", OneComponent.class))
            .isEmpty();

        assertThat(manager.find("InnerComponent_not_exists", OneComponent.InnerComponent.class))
            .isEmpty();
    }

    @Test
    void getOneByClass() {
        OneComponent res;
        res = manager.getOneByClass("OneComponent", OneComponent.class);

        assertThat(res)
            .isNotNull()
            .isInstanceOf(OneComponent.class);
        assertThat(res.getElementOne()).isNull();
        assertThat(res.getInner()).isNull();

        res.initAllElements();
        assertThat(res.getElementOne())
            .isNotNull()
            .isInstanceOf(Input.class);
        assertThat(res.getInner())
            .isNotNull()
            .isInstanceOf(OneComponent.InnerComponent.class);
        assertThat(res.getInner().getInnerElementTwo())
            .isNotNull()
            .isInstanceOf(Element.class);
    }

    @Test
    void getListByClass() {
        List<? extends BaseComponent> res;
        res = manager.getListByClass("OneComponent", OneComponent.class);
        assertThat(res).hasSize(1);

        res = manager.getListByClass("InnerComponent", BaseComponent.class);
        assertThat(res).hasSize(1);

        res = manager.getListByClass("AInnerComponent", BaseComponent.class);
        assertThat(res).hasSize(2);
    }

    @Test
    void getCurrent() {
        assertThat(manager.getCurrent()).isNull();

        manager.getOneByClass("OneComponent", BaseComponent.class);
        assertThat(manager.getCurrent()).isInstanceOf(OneComponent.class);

        manager.getOneByClass("InnerComponent", BaseComponent.class);
        assertThat(manager.getCurrent()).isInstanceOf(OneComponent.InnerComponent.class);
    }

    @Test
    void getCurrentList() {
        assertThat(manager.getCurrentList()).isNull();

        manager.getListByClass("OneComponent", BaseComponent.class);
        assertThat(manager.getCurrentList()).hasSize(1);

        manager.getListByClass("AInnerComponent", BaseComponent.class);
        assertThat(manager.getCurrentList()).hasSize(2);

        manager.getListByClass("InnerComponent", BaseComponent.class);
        assertThat(manager.getCurrentList()).hasSize(1);
    }
}