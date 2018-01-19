package ru.mk.pump.web.elements.api.concrete;

import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.Download;

public interface Link extends Element, Download {

    String getHref();

}
