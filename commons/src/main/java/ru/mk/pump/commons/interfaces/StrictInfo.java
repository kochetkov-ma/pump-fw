package ru.mk.pump.commons.interfaces;

import java.util.Map;

public interface StrictInfo {

    /**
     *  Recommend using <b>Linked</b>Map implementation
     * @return Map of information about Object like {@link #toString()}
     */
    Map<String, String> getInfo();
}
