package ru.mk.pump.commons.utils;

import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Objects {

    @Nullable
    public static <T> T cast(@Nullable Object object, @NonNull Class<T> expectedClass) {
        if (object == null) {
            return null;
        }
        if (expectedClass.isAssignableFrom(object.getClass())) {
            //noinspection unchecked
            return (T) object;
        } else {
            throw new IllegalArgumentException(
                String.format("Object '%s' is not assignable from expected class '%s'", Str.toString(object), expectedClass.getCanonicalName()));
        }
    }
}
