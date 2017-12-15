package ru.mk.pump.web.elements.api.part;

import java.util.Map;
import ru.mk.pump.commons.activity.Parameter;

/**
 * [RUS]
 * Редактируемый элемент. Очень общий метод для любого редактируемого элемента в том числе для DropDown и т.п.
 * Нужен для унифицированных шагов действий над элементов
 */
@SuppressWarnings("unused")
public interface Editable {

    /**
     * [RUS]
     * Общий метод для установки значения в элемент
     * @param params Основное значение для установки + любые дополнительные параметры.
     *      Проверка обязательных параметров осуществляется в реализации каждого конкретного элемента
     */
    void set(Map<String, Parameter<?>> params);
}
