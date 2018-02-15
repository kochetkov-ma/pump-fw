package ru.mk.pump.web.common.api;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.NonNull;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.web.elements.ElementImplDispatcher.ElementImpl;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.BaseElement;

public interface PageItemImplDispatcher extends StrictInfo {

     <R extends BaseElement> ElementImpl<R> findImplementation(@NonNull Class<? extends Element> elementInterface,
        @Nullable Set<Class<? extends Annotation>> requirements);
}
