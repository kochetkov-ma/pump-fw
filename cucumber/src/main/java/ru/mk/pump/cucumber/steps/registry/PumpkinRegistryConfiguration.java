package ru.mk.pump.cucumber.steps.registry;

import static java.util.Locale.ENGLISH;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterByTypeTransformer;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.web.interpretator.items.Item;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Queue;

@Slf4j
public class PumpkinRegistryConfiguration implements TypeRegistryConfigurer {

    private final ParameterByTypeTransformer transformer = new PumpkinTransformer();

    @Override
    public Locale locale() {
        return ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.setDefaultParameterTransformer(transformer);
    }

    private static class PumpkinTransformer implements ParameterByTypeTransformer {

        final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public Object transform(String fromValue, Type toValueType) {
            final Queue<Item> items = CucumberCore.instance().paramParser().generateItems(fromValue);
            if (items.isEmpty()) {
                return null;
            } else {
                return parse(items, toValueType);
            }
        }

        private Object parse(Queue<Item> itemQueue, Type expectedType) {
            Object res;
            if (itemQueue.size() == 1) {
                res = itemQueue.poll().getSource();
            } else {
                StringBuilder str = new StringBuilder();
                for (Item item : itemQueue) {
                    if (item.getSource() instanceof String) {
                        str.append(item.getSource());
                    }
                }
                res = str.toString();
            }
            if (res instanceof String) {
                res = objectMapper.convertValue(res, objectMapper.constructType(expectedType));
            }
            log.debug("[TRANSFORMER] Result : '{}'", res);
            return res;
        }
    }
}