package ru.mk.pump.web.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.config.Property;

@SuppressWarnings("WeakerAccess")
@Data
@NoArgsConstructor
public class ElementConfiguration {

    /**
     * [RUS] Если значение больше -1, то включается проверка размеров элемента.
     * Ширина элемента не должна превышать ширину экрана.
     * Значение указывает максимально допустимое расхождение.
     * <ul>
     *     <li>менее 0 <b>default</b> - проверка отключена. Для приложений с горизонтальным скроллом. </li>
     *     <li>0 - строгая проверка. Элемент не может выходить за пределы экрана справа</li>
     *     <li>25 - нестрогая проверка. Элемент не может выходить за пределы экрана справа более чем на 25px</li>
     *     <li>более 25 - это формальная проверка, что интерфейс совсем не развалился</li>
     * </ul>
     */
    @Property(value = "window.check.width-offset", defaultValue = "0", required = false)
    private int windowWidthOffset;

    @Property(value = "state.reporting", defaultValue = "false", required = false)
    private boolean stateReporting;

    @Property(value = "action.reporting", defaultValue = "false", required = false)
    private boolean actionReporting;
}