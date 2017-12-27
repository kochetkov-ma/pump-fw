package ru.mk.pump.web.constants;

import java.util.Collections;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.apache.commons.text.WordUtils;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.enums.SelectedStrategy;

@UtilityClass
public class ElementParams {

    //region InputDropDown
    /**
     * drop down element parameter is array of {@link org.openqa.selenium.By} in InputDropDownImpl
     */
    public static final String INPUTDROPDOWN_DROPDOWN_BY = "iddDropDownBy";
    /**
     * input element parameter is array of {@link org.openqa.selenium.By} in InputDropDownImpl
     */
    public static final String INPUTDROPDOWN_INPUT_BY = "iddInputBy";
    /**
     * load icon element parameter is array of {@link org.openqa.selenium.By} in InputDropDownImpl (when ajax list of element loading too long)
     */
    public static final String INPUTDROPDOWN_LOAD_BY = "iddLoadBy";
    //endregion


    /**
     * is element's sub items static
     */
    public static final String STATIC_ITEMS = "staticItems";
    /**
     * {@link ru.mk.pump.web.elements.api.part.Editable#set(Map)} parameter name
     */
    public static final String EDITABLE_SET = "setValue";

    /**
     * {@link InputDropDown#set(Map)}  parameter name to find item
     */
    public static final String INPUT_DROPDOWN_SET = "setItemValue";

    /**
     * parameter is array of {@link org.openqa.selenium.By} in DropDownImpl
     */
    public static final String DROPDOWN_EXPAND_BY = "extraBy";

    /**
     * parameter is {@link org.openqa.selenium.By} in BaseElement
     */
    public static final String BASE_EXTRA_BY = "extraBy";

    /**
     * parameter is enum {@link SelectedStrategy}
     */
    public static final String SELECTED_STRATEGY = "selectedStrategy";

    /**
     * parameter is {@link String} contains in element class
     */
    public static final String SELECTED_CONDITION = "selectedCondition";

    public static <T extends Enum<T>> Map<String, Parameter<?>> enumAsParam(T enumInstance) {
        return Collections
            .singletonMap(WordUtils.uncapitalize(enumInstance.getDeclaringClass().getSimpleName()),
                Parameter.of(enumInstance.getDeclaringClass(), enumInstance, enumInstance.toString()));
    }

}
