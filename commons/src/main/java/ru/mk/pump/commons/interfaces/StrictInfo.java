package ru.mk.pump.commons.interfaces;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public interface StrictInfo {

    static ImmutableMap.Builder<String, String> infoBuilder(String type) {
        return ImmutableMap.<String, String>builder()
            .put("type", type);
    }

    Map<String, String> getInfo();
}
