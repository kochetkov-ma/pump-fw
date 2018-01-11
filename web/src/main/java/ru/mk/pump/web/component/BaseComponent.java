package ru.mk.pump.web.component;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.component.api.Component;
import ru.mk.pump.web.common.pageobject.Initializer;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.ElementImplDispatcher;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.page.api.Page;


@SuppressWarnings({"unused", "WeakerAccess"})
@Slf4j
public class BaseComponent extends BaseElement implements Component {

    private Initializer initializer;

    private ElementFactory selfComponentFactory;

    public BaseComponent(By avatarBy, Page page) {
        super(avatarBy, page);
    }

    public BaseComponent(By avatarBy, InternalElement parentElement) {
        super(avatarBy, parentElement);
    }

    public BaseComponent(By avatarBy, Browser browser) {
        super(avatarBy, browser);
    }

    @Override
    public Initializer getInitializer() {
        if (initializer == null) {
            initializer = new Initializer(getSubElementFactory(), getComponentFactory(), this);
        }
        return initializer;
    }

    @Override
    protected ElementFactory getSubElementFactory() {
        if (getPage() != null) {
            return new ElementFactory(new ElementImplDispatcher(), getPage());
        } else {
            return new ElementFactory(new ElementImplDispatcher(), getBrowser());
        }
    }

    protected ElementFactory getComponentFactory() {
        if (selfComponentFactory == null) {
            if (getPage() != null) {
                selfComponentFactory = new ElementFactory(Component.getImplDispatcher(), getPage());
            } else {
                selfComponentFactory = new ElementFactory(Component.getImplDispatcher(), getBrowser());
            }
        }
        return selfComponentFactory;
    }
}