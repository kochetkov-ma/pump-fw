package ru.mk.pump.web.elements.api.part;

import lombok.NonNull;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.web.elements.internal.DocParameters;

/**
 * [RUS]
 * Редактируемый элемент. Очень общий метод для любого редактируемого элемента в том числе для DropDown и т.п.
 * Нужен для унифицированных шагов действий над элементов
 */
@SuppressWarnings("unused")
@DocParameters({"EDITABLE_SET", "INPUT_DROPDOWN_SET_STRING", "INPUT_DROPDOWN_SET_NUMBER"})
public interface Editable {

    /**
     * [RUS]
     * Общий метод для установки значения в элемент
     *
     * @param params Основное значение для установки + любые дополнительные параметры.
     *               Проверка обязательных параметров осуществляется в реализации каждого конкретного элемента
     */
    void set(Parameters params);

    default void set(@NonNull Parameter... parameters) {
        set(Parameters.of(parameters));
    }
}
