package ru.mk.pump.web.component.api;

import ru.mk.pump.web.elements.api.Element;

public interface Component {

    void initAllElements();

    void initElementsByClass(Class<? extends Element> initClass);
}
