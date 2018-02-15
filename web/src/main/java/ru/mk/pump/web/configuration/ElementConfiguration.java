package ru.mk.pump.web.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.config.Property;

@SuppressWarnings("WeakerAccess")
@Data
@NoArgsConstructor
public class ElementConfiguration {

    /**
     * [RUS]
     * Если значение больше -1, то включается проверка размеров элемента.
     * Ширина элемента не должна превышать ширину экрана.
     * Значение указывает максимально допустимое расхождение.
     * <ul>
     *     <li>менее 0 <b>default</b> - проверка отключена. Для приложений с горизонтальным скроллом. </li>
     *     <li>0 - строгая проверка. Элемент не может выходить за пределы экрана справа</li>
     *     <li>25 - нестрогая проверка. Элемент не может выходить за пределы экрана справа более чем на 25px</li>
     *     <li>более 25 - это формальная проверка, что интерфейс совсем не развалился</li>
     * </ul>
     */
    @Property(value = "window.check.width-offset", defaultValue = "0")
    private int windowWidthOffset;

    /**
     * [RUS]
     * Вкл/Выкл репортинг проверок состояния элемента {@link ru.mk.pump.web.elements.internal.StateResolver}
     */
    @Property(value = "state.reporting", defaultValue = "false")
    private boolean stateReporting;

    /**
     * [RUS]
     * Вкл/Выкл репортинг дейсвтий над жлементами через {@link ru.mk.pump.web.elements.internal.ActionExecutor}
     */
    @Property(value = "action.reporting", defaultValue = "false")
    private boolean actionReporting;

    /**
     * [RUS]
     * Мс. Промежуток между каждой попыткой проверки окончания изменения подэлементов
     */
    @Property(value = "element.items.changing.poll-delay", defaultValue = "200")
    private int subItemsChangingPoolDelay;

    /**
     * [RUS]
     * Мс. Таймаут проверки окончания изменения подэлементов
     */
    @Property(value = "element.items.changing.timeout", defaultValue = "1000")
    private int subItemsChangingTimeout;

    /**
     * [RUS]
     * Сек. Таймаут проверки состояния элемента
     */
    @Property(value = "element.state.timeout", defaultValue = "30")
    private int stateTimeout;
}