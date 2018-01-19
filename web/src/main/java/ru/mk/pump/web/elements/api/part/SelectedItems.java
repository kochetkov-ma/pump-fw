package ru.mk.pump.web.elements.api.part;

import java.util.List;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.elements.api.Element;

/**
 * [RUS] Общий иннтерфейс для элементов, где присутствует список под-элементов с выбором ОДНОГО элемента из списка
 */
//TODO:: Необходимо выделить интерфейс для списка под-элементов + интерфейс с множественным селектом
@SuppressWarnings({"unused", "WeakerAccess"})
public interface SelectedItems extends Editable {

    void select(String itemText);

    void select(int index);

    Element getSelected();

    /**
     * [RUS] Получить список под-элементов для выбора.<br/>
     * В стандартной реализации возвращается значение из кэша, если кэш не равен null или не пустой.<br/>
     * Если список динамический, для отключения кэша необходимо установить элементу параметр {@link ru.mk.pump.web.constants.ElementParams#SELECTOR_STATIC_ITEMS}
     */
    List<Element> getItems();

    /**
     * [RUS] Проверить, что существует указанное кол-во под-элементов.
     * Это состояние элемента проверяется в ручном режиме
     * (т.е. данный метод в стандартной реализации элемента не вызывается), т.к. существует много вариантов ДронДаунов, когда кол-во элементов рано 0.
     *
     * @param count Ожидаемое минимальное количество под-элементов
     * @param timeoutMs Тайм аут ms
     * @return Результат ожидания
     */
    WaitResult<Boolean> hasItems(int count, int timeoutMs);

    /**
     * [RUS] Проверить, что существует указанное кол-во под-элементов.
     * Это состояние элемента проверяется в ручном режиме
     * (т.е. данный метод в стандартной реализации элемента не вызывается), т.к. существует много вариантов ДронДаунов, когда кол-во элементов рано 0.
     *
     * @param count Ожидаемое минимальное количество под-элементов
     * @return Результат ожидания
     */
    WaitResult<Boolean> hasItems(int count);
}
