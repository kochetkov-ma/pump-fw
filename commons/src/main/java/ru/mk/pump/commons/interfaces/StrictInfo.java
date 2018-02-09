package ru.mk.pump.commons.interfaces;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public interface StrictInfo {

    static ImmutableMap.Builder<String, String> infoBuilder(String type) {
        return ImmutableMap.<String, String>builder()
            .put("type", type);
    }

    static ImmutableMap.Builder<String, String> infoFromSuper(StrictInfo thisInstance, Map<String,String> superInfo) {
        return ImmutableMap.<String, String>builder()
            .putAll(superInfo)
            .put("type", thisInstance.getClass().getSimpleName());
    }

    Map<String, String> getInfo();
}
