package ru.mk.pump.commons.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@SuppressWarnings({"WeakerAccess", "unused"})
@UtilityClass
public class Preconditions {

    public void checkNotEmpty(@Nullable Object[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(String.format("Checked array '%s' is nonArg", Strings.toString(array)));
        }
    }

    public void checkNotEmpty(@Nullable Map map) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(String.format("Checked map '%s' is nonArg", Strings.toString(map)));
        }
    }

    public void checkNotEmpty(@Nullable Collection collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(String.format("Checked collection '%s' is nonArg", Strings.toString(collection)));
        }
    }

    public void checkObjectNotNull(@Nullable Object object, @NonNull Class aClass, String msg) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException(String.format(Strings.concat(".", msg, "Checked object '%s' is null"), aClass.getCanonicalName()));
        }
    }

    public void checkObjectNotNull(@Nullable Object object, @NonNull Class aClass) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException(String.format("Checked object '%s' is null", aClass.getCanonicalName()));
        }
    }

    public void checkStringNotBlank(@Nullable String string) {
        if (Strings.isEmpty(string)) {
            throw new IllegalArgumentException(String.format("Checked string '%s' is blank", string));
        }
    }

    public void checkArgListSize(int candidateIndex, int listSize, String listDescriptionOrName) {
        if (candidateIndex >= listSize) {
            if (!Strings.isEmpty(listDescriptionOrName)) {
                throw new IllegalArgumentException(String
                    .format("Requested index '%d' is out of actual list size '%d'. List description : '%s'", listSize, candidateIndex, listDescriptionOrName));
            } else {
                throw new IllegalArgumentException(String
                    .format("Requested index '%d' is out of actual list size '%d'", candidateIndex, listSize));
            }
        }
    }

    public void checkArgListSize(int candidateIndex, List<?> list) {
        checkArgListSize(candidateIndex, list.size(), list.getClass().getSimpleName());
    }
}