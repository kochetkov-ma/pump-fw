package ru.mk.pump.web.constants;

import java.util.Collections;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.apache.commons.text.WordUtils;
import ru.mk.pump.commons.activity.Parameter;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.enums.FocusType;
import ru.mk.pump.web.elements.enums.SelectedStrategy;

@UtilityClass
public class ElementParams {
    //region InputDropDown

    /**
     * Defined optional value for select in DropDown.
     * Parameter is {@link String} (select by name) or {@link Integer} (select by index).
     * Used in {@link InputDropDown#set(Map)}.
     */
    public static final String INPUT_DROPDOWN_SET = "setItemValue";

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

    //region AbstractSelectorItems in DropDown and Selector

    /**
     * parameter is enum {@link SelectedStrategy}
     */
    public static final String SELECTED_STRATEGY = "selectedStrategy";

    /**
     * Marker in element css class when it was selected.
     * Parameter is {@link String} in Selector
     */
    public static final String SELECTED_MARK = "selectedCondition";

    /**
     * parameter is {@link org.openqa.selenium.By}[] in AbstractSelectorItems (any rules with items set like DropDown or Selector)
     */
    public static final String SELECTOR_ITEMS_BY = "sItemsBy";

    /**
     * Is element's sub items static. Parameter is {@link Boolean} in AbstractSelectorItems
     */
    public static final String SELECTOR_STATIC_ITEMS = "sStaticItems";
    //endregion

    //region DropDown interface

    /**
     * parameter is array of {@link org.openqa.selenium.By} in DropDownImpl
     */
    public static final String DROPDOWN_EXPAND_BY = "extraBy";

    /**
     * parameter is {@link Boolean} in DropDownImpl. Internal usage
     */
    public static final String DROPDOWN_BEFORE_SELECT = "beforeSelect";
    //endregion

    /**
     * Defined required value to set in any Editable element.
     * {@link ru.mk.pump.web.elements.api.part.Editable#set(Map)} parameter name
     */
    public static final String EDITABLE_SET = "setValue";

    /**
     * If standard {@link FocusType} is not correct for your JS app, you can override scroll script.
     * Parameter is {@link String} - JS script
     */
    public static final String FOCUS_CUSTOM_SCRIPT = "focusCustomScript";

    public static <T extends Enum<T>> Map<String, Parameter<?>> enumAsParam(T enumInstance) {
        return Collections
            .singletonMap(WordUtils.uncapitalize(enumInstance.getDeclaringClass().getSimpleName()),
                Parameter.of(enumInstance.getDeclaringClass(), enumInstance, enumInstance.toString()));
    }

}
