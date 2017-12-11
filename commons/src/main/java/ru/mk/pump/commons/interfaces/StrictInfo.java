package ru.mk.pump.commons.interfaces;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public interface StrictInfo {

    static ImmutableMap.Builder<String, String> infoBuilder(String type) {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.<String, String>builder()
            .put("type", type);
        return builder;
    }

    Map<String, String> getInfo();
}
