package ru.mk.pump.web.component.api;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.support.PageFactory;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.common.api.PageItem;
import ru.mk.pump.web.common.api.PageItemImplDispatcher;
import ru.mk.pump.web.common.pageobject.Initializer;
import ru.mk.pump.web.elements.ElementImplDispatcher.ElementImpl;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.BaseElement;

public interface Component extends PageItem{

    default void initAllElements() {
        PageFactory.initElements(getInitializer(), this);
    }

    default void initElementsByClass(Class<? extends Element> initClass) {
        PageFactory.initElements(getInitializer().withClassFilter(initClass), this);
    }

    static PageItemImplDispatcher getImplDispatcher() {
        return new PageItemImplDispatcher() {
            @Override
            public <R extends BaseElement> ElementImpl<R> findImplementation(@NotNull Class<? extends Element> elementInterface,
                @Nullable Set<Class<? extends Annotation>> requirements) {
                try {
                    return ElementImpl.of((Class<R>) elementInterface, null);
                } catch (ClassCastException ex) {
                    throw new InternalError("Error in Component Dispatcher", ex);
                }
            }

            @Override
            public Map<String, String> getInfo() {
                return StrictInfo.infoBuilder("component impl dispatcher").build();
            }
        };
    }

    Initializer getInitializer();


}
