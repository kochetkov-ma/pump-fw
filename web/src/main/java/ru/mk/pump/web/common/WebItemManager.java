package ru.mk.pump.web.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.component.BaseComponent;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.interpretator.rules.Pumpkin;
import ru.mk.pump.web.page.BasePage;
import ru.mk.pump.web.page.PageManager;
import ru.mk.pump.web.page.api.Page;

public class WebItemManager {

    private final PageManager pageManager;

    private final Pumpkin pumpkin;

    private BasePage currentPage;

    private BaseElement currentElement;

    private BaseComponent currentComponent;

    public WebItemManager(PageManager pageManager, Pumpkin pumpkin) {
        this.pageManager = pageManager;
        this.pumpkin = pumpkin;
    }

    public Element getElement(String pumpkinExpression) {
        return null;
    }

    public Page getPage(String pumpkinExpression) {
        return null;
    }

    public Element getComponent(String pumpkinExpression) {
        return null;
    }

    public Object callMethod(String pumpkinExpression) {
        return null;
    }

    @Nullable
    public static <T> T checkAndCast(@Nullable Object object, @NotNull Class<T> expectedClass) {
        if (object == null) {
            return null;
        }
        if (expectedClass.isAssignableFrom(object.getClass())) {
            //noinspection unchecked
            return (T) object;
        } else {
            throw new IllegalArgumentException(
                String.format("Object '%s' is not assignable from expected class '%s'", Strings.toString(object), expectedClass.getCanonicalName()));
        }

    }

}
