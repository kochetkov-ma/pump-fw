package ru.mk.pump.web.elements;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.ElementImplDispatcher.ElementImpl;
import ru.mk.pump.web.elements.api.annotations.Date;
import ru.mk.pump.web.elements.api.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.annotations.Requirements;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.Image;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ALL")
@Slf4j
class ElementImplDispatcherTest {


    private ElementImplDispatcher dispatcher;

    static class ImageImpl extends BaseElement implements Image {

        public ImageImpl(By avatarBy, Page page) {
            super(avatarBy, page);
        }

        public ImageImpl(By avatarBy, InternalElement parentElement) {
            super(avatarBy, parentElement);
        }

        public ImageImpl(By avatarBy, Browser browser) {
            super(avatarBy, browser);
        }
    }

    @ToTest
    @FrameworkImpl
    static class InputComplexImpl extends BaseElement implements Input {

        public InputComplexImpl(By avatarBy, Page page) {
            super(avatarBy, page);
        }

        public InputComplexImpl(By avatarBy, InternalElement parentElement) {
            super(avatarBy, parentElement);
        }

        public InputComplexImpl(By avatarBy, Browser browser) {
            super(avatarBy, browser);
        }

        @Override
        public String type(String... text) {
            return null;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Requirements
    @interface ToTest {

    }

    @BeforeEach
    void setUp() {
        dispatcher = new ElementImplDispatcher();
        dispatcher.loadDefault();
    }

    @Test
    void findImplementation() {

        ElementImpl<BaseElement> res = dispatcher.findImplementation(Input.class, Sets.newHashSet(Date.class));
        assertThat(res).matches(i -> StringUtils.containsIgnoreCase(i.getImplementation().getSimpleName(), "DataInputImpl"));

        res = dispatcher.findImplementation(Button.class, Sets.newHashSet(Date.class));
        assertThat(res).matches(i -> StringUtils.containsIgnoreCase(i.getImplementation().getSimpleName(), "ButtonImpl"));

        res = dispatcher.findImplementation(DropDown.class, null);
        assertThat(res).matches(i -> StringUtils.containsIgnoreCase(i.getImplementation().getSimpleName(), "DropDownImpl"));

        res = dispatcher.findImplementation(Input.class, Sets.newHashSet(FrameworkImpl.class, ToTest.class));
        assertThat(res).matches(i -> StringUtils.containsIgnoreCase(i.getImplementation().getSimpleName(), "InputImpl"));
    }

    @Test
    void addImplementation() {
        dispatcher.addImplementation(Image.class, ElementImpl.of(ImageImpl.class, null));
        dispatcher.addImplementation(Input.class, ElementImpl.of(InputComplexImpl.class, null));

        ElementImpl<BaseElement> res = dispatcher.findImplementation(Image.class, null);
        assertThat(res).matches(i -> StringUtils.containsIgnoreCase(i.getImplementation().getSimpleName(), "ImageImpl"));

        res = dispatcher.findImplementation(Input.class, Sets.newHashSet(ToTest.class));
        assertThat(res).matches(i -> StringUtils.containsIgnoreCase(i.getImplementation().getSimpleName(), "InputComplexImpl"));
    }

    @Test
    void getAll() {
        dispatcher.addImplementation(Image.class, ElementImpl.of(ImageImpl.class, null));
        dispatcher.addImplementation(Input.class, ElementImpl.of(InputComplexImpl.class, null));
        dispatcher.addImplementation(Image.class, ElementImpl.of(ImageImpl.class, null));
        dispatcher.addImplementation(Input.class, ElementImpl.of(InputComplexImpl.class, null));

        log.info(Strings.toPrettyString(dispatcher.getInfo()));
    }

    @Test
    void addParams() {
        dispatcher.addImplementation(Image.class, ElementImpl.of(ImageImpl.class, Parameters.of(Parameter.of("testParam", "тестовый"))));

        ElementImpl<BaseElement> res = dispatcher.findImplementation(Image.class, null);
        log.info(res.toString());
    }

    @Test
    void getInfo() {
        log.info(System.lineSeparator() + Strings.toPrettyString(dispatcher.getInfo()));
    }

    @Test
    void loadDefault() {
        log.info(System.lineSeparator() + Strings.toPrettyString(dispatcher.getInfo()));
        dispatcher.loadDefault();
        log.info(System.lineSeparator() + Strings.toPrettyString(dispatcher.getInfo()));
    }

    @Test
    void testToString() {
        log.info(dispatcher.toString());
    }
}