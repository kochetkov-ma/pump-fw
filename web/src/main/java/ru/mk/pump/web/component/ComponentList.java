package ru.mk.pump.web.component;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import ru.mk.pump.web.common.AbstractPageItemList;
import ru.mk.pump.web.component.api.Component;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.ElementConfig;
import ru.mk.pump.web.elements.internal.BaseElement;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString
@Slf4j
public class ComponentList<T extends Component & Element> extends AbstractPageItemList<T> {

    private Class<? extends Element> initClass;

    public ComponentList(@NotNull Class<T> itemsClass, @NotNull By listBy, @NotNull ElementFactory componentFactory, @NotNull ElementConfig elementConfig) {
        super(itemsClass, listBy, componentFactory, elementConfig);
    }

    public ComponentList(@NotNull Class<T> itemsClass, @NotNull By listBy, @Nullable BaseElement parent, @NotNull ElementFactory componentFactory,
        @NotNull ElementConfig elementConfig) {
        super(itemsClass, listBy, parent, componentFactory, elementConfig);
    }

    public ComponentList<T> withInitFilter(Class<? extends Element> initClass) {
        this.initClass = initClass;
        return this;
    }

    /**
     * [RUS] Каждый вызов создает новый компонент указанного в конструкторе класса. Устанавливает компоненту индекс из параметров метода.
     * Далее происходит иницализация полей компонента. Созданный компонент также является элементом.
     * Созданный компонент(элемент) может не существовать, передавайемый в параметрах индекс может быть больше размера массива.
     * Корректность можно проверить выполнив {@link #size()} либо дальнейшие манипуляции с полученным компонент(элемент).
     * @param index Предполагаемый индекс будущего компонента(элемента)
     * @return Инициализированный компонент(элемент)
     */
    @Override
    public T get(int index) {
        final T singleElement;
        if (hasCache(index)) {
            log.debug("Get item in ComponentList of {} from cache index {}",itemsClass, index);
            return getCache(index);
        }
        if (parent != null) {
            singleElement = itemFactory.newElement(itemsClass, listBy, parent, elementConfig);
        } else {
            singleElement = itemFactory.newElement(itemsClass, listBy, elementConfig);
        }
        ((BaseElement) singleElement).setIndex(index);
        log.debug("Create new item in ComponentList {} new element {} with index {}", itemsClass, singleElement, index);
        if (initClass == null) {
            singleElement.initAllElements();
        } else {
            singleElement.initElementsByClass(initClass);
        }
        log.debug("Save new item ComponentList {} save cache index {}", itemsClass, index);
        return saveCache(index, singleElement);
    }
}