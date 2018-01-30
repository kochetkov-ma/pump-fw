package ru.mk.pump.web.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.config.Property;


@Getter
@NoArgsConstructor
public class Configuration {

    /**
     * Имя тестируемого приложения для отчетов
     */
    @Property("application.name")
    private String applicationName;

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
    @Property(value = "element.window-size.offset.checking", defaultValue = "0", required = false)
    private int windowSizeOffset;

    public Configuration withApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public Configuration withWindowSizeOffset(int windowSizeOffset) {
        this.windowSizeOffset = windowSizeOffset;
        return this;
    }
}
