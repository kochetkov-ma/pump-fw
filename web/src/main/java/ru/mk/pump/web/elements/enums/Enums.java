package ru.mk.pump.web.elements.enums;

import java.util.Collections;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.WordUtils;
import ru.mk.pump.commons.activity.Parameter;

@SuppressWarnings("WeakerAccess")
@UtilityClass
@Slf4j
public class Enums {

    public static  <T extends Enum<T>> Map<String, Parameter<T>> asParam(T enumInstance) {
        return Collections.singletonMap(WordUtils.uncapitalize(enumInstance.getDeclaringClass().getSimpleName()), Parameter.of(enumInstance.getDeclaringClass(), enumInstance, enumInstance.toString()));
    }
}
