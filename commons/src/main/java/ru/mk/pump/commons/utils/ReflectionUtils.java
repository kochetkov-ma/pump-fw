package ru.mk.pump.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ReflectionUtils {

    public Class<?> getGenericParameterField(Field field, int parameterIndex) {
        final Type genericType = field.getGenericType();
        return (Class<?>) (((ParameterizedType) genericType).getActualTypeArguments()[parameterIndex]);
    }

    /**
     * Check type signature for exists {@code sourceSuperclassOrInterface}. It is not {@link Class#isAssignableFrom(Class)}.
     * Checked only one root level of class hierarchy
     * @param sourceSuperclassOrInterface superclass or interface in {@code checkedClass} signature
     * @param checkedClass the {@code Class} object to be checked
     */
    public boolean hasInterfaceOrSuperclass(@NotNull Class<?> sourceSuperclassOrInterface, @NotNull Class<?> checkedClass) {
        return checkedClass.getSuperclass() == sourceSuperclassOrInterface || Arrays.stream(checkedClass.getInterfaces())
            .anyMatch(cls -> cls == sourceSuperclassOrInterface);
    }
}
