package ru.mk.pump.web.elements.api.part;

import java.util.List;
import ru.mk.pump.web.elements.api.Element;

@SuppressWarnings({"unused", "WeakerAccess"})
public interface SelectedItems extends Editable{

    void select(String itemText);

    void select(int index);

    Element getSelected();

    List<Element> getItems();

}
