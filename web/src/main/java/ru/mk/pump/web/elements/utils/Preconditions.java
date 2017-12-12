package ru.mk.pump.web.elements.utils;

import java.util.List;
import lombok.experimental.UtilityClass;
import ru.mk.pump.commons.utils.Strings;

@UtilityClass
public class Preconditions {

    public void checkArgListSize(int candidateIndex, int listSize, String listDescriptionOrName) {
        if (candidateIndex >= listSize) {
            if (!Strings.isEmpty(listDescriptionOrName)) {
                throw new IllegalArgumentException(String
                    .format("Requested index '%d' is out of actual list size '%d'. List description : '%s'", listSize, candidateIndex, listDescriptionOrName));
            } else {
                throw new IllegalArgumentException(String
                    .format("Requested index '%d' is out of actual list size '%d'", listSize, candidateIndex));
            }
        }
    }

    public void checkArgListSize(int candidateIndex, List<?> list) {
        checkArgListSize(candidateIndex, list.size(), list.getClass().getSimpleName());
    }
}
