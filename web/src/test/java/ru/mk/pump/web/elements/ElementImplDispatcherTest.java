package ru.mk.pump.web.elements;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.elements.ElementImplDispatcher.ElementImpl;
import ru.mk.pump.web.elements.annotations.Date;
import ru.mk.pump.web.elements.annotations.FrameworkImpl;
import ru.mk.pump.web.elements.api.concrete.Button;
import ru.mk.pump.web.elements.api.concrete.DropDown;
import ru.mk.pump.web.elements.api.concrete.Image;
import ru.mk.pump.web.elements.api.concrete.Input;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;

@Slf4j
public class ElementImplDispatcherTest {


    private ElementImplDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        dispatcher = new ElementImplDispatcher();
        dispatcher.loadDefault();
    }

    @Test
    public void findImplementation() {

        ElementImpl<BaseElement> res = dispatcher.findImplementation(Input.class, Sets.newHashSet(Date.class));
        log.info(res.toString());

        res = dispatcher.findImplementation(Button.class, Sets.newHashSet(Date.class));
        log.info(res.toString());

        res = dispatcher.findImplementation(DropDown.class, null);
        log.info(res.toString());

        res = dispatcher.findImplementation(Input.class, Sets.newHashSet(FrameworkImpl.class, ToTest.class));
        log.info(res.toString());
    }

    @Test
    public void addImplementation() {
        dispatcher.addImplementation(Image.class, ElementImpl.of(ImageImpl.class, null));
        dispatcher.addImplementation(Input.class, ElementImpl.of(InputComplexImpl.class, null));

        ElementImpl<BaseElement> res = dispatcher.findImplementation(Image.class, null);
        log.info(res.toString());

        res = dispatcher.findImplementation(Input.class, Sets.newHashSet(ToTest.class));
        log.info(res.toString());
    }

    @Test
    public void getAll() {
        dispatcher.addImplementation(Image.class, ElementImpl.of(ImageImpl.class, null));
        dispatcher.addImplementation(Input.class, ElementImpl.of(InputComplexImpl.class, null));
        dispatcher.addImplementation(Image.class, ElementImpl.of(ImageImpl.class, null));
        dispatcher.addImplementation(Input.class, ElementImpl.of(InputComplexImpl.class, null));

        log.info(Strings.toPrettyString(dispatcher.getInfo()));
    }

    @Test
    public void addParams() {
        dispatcher.addImplementation(Image.class, ElementImpl.of(ImageImpl.class, ImmutableMap.of("testParam", Parameter.of("тестовый"))));

        ElementImpl<BaseElement> res = dispatcher.findImplementation(Image.class, null);
        log.info(res.toString());
    }

    @Test
    public void getInfo() {
        log.info(System.lineSeparator() + Strings.toPrettyString(dispatcher.getInfo()));
    }

    @Test
    public void loadDefault() {
        log.info(System.lineSeparator() + Strings.toPrettyString(dispatcher.getInfo()));
        dispatcher.loadDefault();
        log.info(System.lineSeparator() + Strings.toPrettyString(dispatcher.getInfo()));
    }

    @Test
    public void testToString() {
        log.info(dispatcher.toString());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface ToTest {

    }

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
}