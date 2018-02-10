package ru.mk.pump.commons.interfaces;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public interface StrictInfo {

    static ImmutableMap.Builder<String, String> infoBuilder(String type) {
        return ImmutableMap.<String, String>builder()
                .put("type", type);
    }

    static ImmutableMap.Builder<String, String> infoFromSuper(StrictInfo thisInstance, Map<String, String> superInfo) {
        Map<String, String> res = Maps.newHashMap(superInfo);
        res.put("type", thisInstance.getClass().getSimpleName());
        return ImmutableMap.<String, String>builder().putAll(res);

    }

    Map<String, String> getInfo();
}
