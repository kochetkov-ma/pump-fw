package ru.mk.pump.commons.interfaces;

import java.util.HashMap;
import java.util.Map;

public interface StrictInfo {

    class StringMapBuilder {

        private final Map<String, String> map;

        private StringMapBuilder() {
            this.map = new HashMap<>();
        }

        public Map<String, String> build() {
            return map;
        }

        public StringMapBuilder put(String key, String value) {
            map.put(key, value);
            return this;
        }

        public StringMapBuilder putAll(Map<String, String> stringMap) {
            map.putAll(stringMap);
            return this;
        }
    }

    static StringMapBuilder infoBuilder(String type) {
        return new StringMapBuilder()
            .put("type", type);
    }

    static StringMapBuilder infoFromSuper(StrictInfo thisInstance, Map<String, String> superInfo) {
        return new StringMapBuilder()
            .putAll(superInfo)
            .put("type", thisInstance.getClass().getSimpleName());

    }

    Map<String, String> getInfo();
}
