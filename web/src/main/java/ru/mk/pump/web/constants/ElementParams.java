package ru.mk.pump.web.constants;

import lombok.experimental.UtilityClass;
import org.apache.commons.text.WordUtils;
import org.openqa.selenium.By;
import ru.mk.pump.commons.helpers.Parameter;
import ru.mk.pump.web.elements.api.concrete.complex.InputDropDown;
import ru.mk.pump.web.elements.enums.CheckBoxState;
import ru.mk.pump.web.elements.enums.ClearType;
import ru.mk.pump.web.elements.enums.FocusType;
import ru.mk.pump.web.elements.enums.SelectedStrategy;

@UtilityClass
public class ElementParams {

    /**
     * Defined optional value for select in DropDown.
     * Parameter is {@link String} (select by name)
     * Used in {@link InputDropDown#set(ru.mk.pump.commons.helpers.Parameters)}.
     */
    public static final Parameter<String> INPUT_DROPDOWN_SET_STRING = stringParam("setItemValueString");
    /**
     * Defined optional value for select in DropDown.
     * Parameter {@link Integer} (select by index).
     * Used in {@link InputDropDown#set(ru.mk.pump.commons.helpers.Parameters)}.
     */
    public static final Parameter<Integer> INPUT_DROPDOWN_SET_NUMBER = intParam("setItemValueNumber");
    /**
     * Defined required value to set in any Editable element. Set text
     * {@link ru.mk.pump.web.elements.api.part.Editable#set(ru.mk.pump.commons.helpers.Parameters)} parameter name
     */
    public static final Parameter<String> EDITABLE_SET_STRING = stringParam("setValueString");
    /**
     * Defined required value to set in any Editable element. Set index
     * {@link ru.mk.pump.web.elements.api.part.Editable#set(ru.mk.pump.commons.helpers.Parameters)} parameter name
     */
    public static final Parameter<Integer> EDITABLE_SET_NUMBER = intParam("setValueNumber");
    /**
     * Defined required value to set in any CheckBox element
     * {@link ru.mk.pump.web.elements.api.part.Editable#set(ru.mk.pump.commons.helpers.Parameters)} parameter name
     */
    public static final Parameter<CheckBoxState> EDITABLE_SET_CHECKBOX = enumParam(CheckBoxState.class);

    /**
     * drop down element parameter is array of {@link org.openqa.selenium.By} in InputDropDownImpl
     */
    public static final Parameter<By[]> INPUTDROPDOWN_DROPDOWN_BY = byArrayParam("iddDropDownBy");
    /**
     * input element parameter is array of {@link org.openqa.selenium.By} in InputDropDownImpl
     */
    public static final Parameter<By[]> EXTRA_INPUT_BY = byArrayParam("iddInputBy");
    /**
     * load icon element parameter is array of {@link org.openqa.selenium.By} in InputDropDownImpl (when ajax list of element loading too long)
     */
    public static final Parameter<By[]> INPUTDROPDOWN_LOAD_BY = byArrayParam("iddLoadBy");
    /**
     * parameter is enum {@link SelectedStrategy}
     */
    public static final Parameter<SelectedStrategy> SELECTED_STRATEGY = enumParam(SelectedStrategy.class);

    //region InputDropDown
    /**
     * Marker in element css class when it was selected.
     * Parameter is {@link String} in Selector
     */
    public static final Parameter<String> SELECTED_MARK = stringParam("selectedCondition");
    /**
     * parameter is {@link org.openqa.selenium.By}[] in AbstractSelectorItems (any rules with items set like DropDown or Selector)
     */
    public static final Parameter<By> SELECTOR_ITEMS_BY = byParam("sItemsBy");
    /**
     * Is element's sub items static. Parameter is {@link Boolean} in AbstractSelectorItems
     */
    public static final Parameter<Boolean> SELECTOR_STATIC_ITEMS = boolParam("sStaticItems");
    /**
     * parameter is array of {@link org.openqa.selenium.By} in DropDownImpl
     */
    public static final Parameter<By[]> DROPDOWN_EXPAND_BY = byArrayParam("extraBy");
    //endregion

    //region AbstractSelectorItems in DropDown and Selector
    /**
     * parameter is {@link Boolean} in DropDownImpl. Internal usage
     */
    public static final Parameter<Boolean> DROPDOWN_BEFORE_SELECT = boolParam("beforeSelect");

    /**
     * Parameter is {@link String} in Element.
     * If standard {@link FocusType} is not correct for your JS app, you can override scroll script.
     * Parameter is {@link String} - JS script
     */
    public static final Parameter<String> FOCUS_CUSTOM_SCRIPT = stringParam("focusCustomScript");
    /**
     * Parameter is {@link FocusType} in Element.
     */
    public static final Parameter<FocusType> FOCUS_TYPE = enumParam(FocusType.class);
    //endregion

    //region DropDown interface
    /**
     * Parameter is {@link ClearType} in Element.
     */
    public static final Parameter<ClearType> CLEAR_TYPE = enumParam(ClearType.class);

    public static <T extends Enum<T>> Parameter<T> enumAsParam(T enumInstance) {
        return Parameter.of(WordUtils.uncapitalize(enumInstance.getDeclaringClass().getSimpleName()),
                enumInstance.getDeclaringClass(), enumInstance, enumInstance.name());
    }
    //endregion

    private static Parameter<String> stringParam(String name) {
        return Parameter.of(name, String.class);
    }


    //region common ELEMENT

    private static Parameter<By[]> byArrayParam(String name) {
        return Parameter.of(name, By[].class);
    }

    @SuppressWarnings("SameParameterValue")
    private static Parameter<By> byParam(String name) {
        return Parameter.of(name, By.class);
    }

    private static Parameter<Integer> intParam(String name) {
        return Parameter.of(name, Integer.class);
    }

    private static Parameter<Boolean> boolParam(String name) {
        return Parameter.of(name, Boolean.class);
    }
    //endregion

    private static <T extends Enum<T>> Parameter<T> enumParam(Class<T> enumClass) {
        return Parameter.of(WordUtils.uncapitalize(enumClass.getSimpleName()), enumClass);
    }

}
