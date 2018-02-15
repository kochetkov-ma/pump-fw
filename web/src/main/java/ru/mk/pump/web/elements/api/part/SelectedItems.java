package ru.mk.pump.web.elements.api.part;

import ru.mk.pump.web.elements.api.Element;

/**
 * [RUS] Общий иннтерфейс для элементов, где присутствует список под-элементов с выбором ОДНОГО элемента из списка
 */
//TODO:: Необходимо выделить интерфейс для списка под-элементов + интерфейс с множественным селектом
@SuppressWarnings({"unused", "WeakerAccess"})
public interface SelectedItems extends WithSubItems, Editable {

    void select(String itemText);

    void select(int index);

    Element getSelected();


}
