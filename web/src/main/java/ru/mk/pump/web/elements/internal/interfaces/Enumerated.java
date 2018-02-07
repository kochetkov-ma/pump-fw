package ru.mk.pump.web.elements.internal.interfaces;

public interface Enumerated {

    /**
     * @return <b>true</b> - this element is one of rules list with the same locator / <b>false</b> - single element
     */
    boolean isList();

    /**
     * @return '<b>-1</b>' if {@link #isList()} = false / '<b>>0</b>' if {@link #isList()} = true
     */
    int getIndex();

    InternalElement setIndex(int index);

}
