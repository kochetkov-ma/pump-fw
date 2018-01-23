package ru.mk.pump.web.elements;

import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import ru.mk.pump.web.common.AbstractPageItemList;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.BaseElement;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString
public class ElementList<T extends Element> extends AbstractPageItemList<T> {

    public ElementList(@NotNull Class<T> itemsClass, @NotNull By listBy, @NotNull ElementFactory elementFactory, @NotNull ElementConfig elementConfig) {
        super(itemsClass, listBy, elementFactory, elementConfig);
    }

    public ElementList(@NotNull Class<T> itemsClass, @NotNull By listBy, @Nullable BaseElement parent, @NotNull ElementFactory elementFactory,
        @NotNull ElementConfig elementConfig) {
        super(itemsClass, listBy, parent, elementFactory, elementConfig);
    }

    /**
     * [RUS] Каждый вызов создает новый элемент указанного в конструкторе класса. Устанавливает элементу индекс из параметров метода.
     * Созданный элемент может не существовать, передавайемый в параметрах индекс может быть больше размера массива.
     * Корректность можно проверить выполнив {@link #size()} либо дальнейшие манипуляции с полученным элементом
     * @param index Предполагаемый индекс будущего элемента
     * @return Инициализированный элемент
     */
    @Override
    public T get(int index) {
        final T singleElement;
        if (hasCache(index)) {
            return getCache(index);
        }
        if (parent != null) {
            singleElement = itemFactory.newElement(itemsClass, listBy, parent, elementConfig);
        } else {
            singleElement = itemFactory.newElement(itemsClass, listBy, elementConfig);
        }
        ((BaseElement)singleElement).setIndex(index);
        return saveCache(index, singleElement);
    }
}