package ru.mk.pump.cucumber.glue.other.registry;

import io.cucumber.cucumberexpressions.ParameterByTypeTransformer;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.JavaType;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.web.interpretator.items.Item;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
@Slf4j
public class PumpkinTransformer implements ParameterByTypeTransformer {

    private final String delimiter = ",";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object transform(String fromValue, Type toValueType) {
        if (fromValue == null) {
            return parseNonPumpkin(null, toValueType);
        }
        fromValue = new StringSubstitutor(CucumberCore.instance().getTestVariables().getStringMap(), "${str:", "}").replace(fromValue);
        final Queue<Item> items = getItems(fromValue);
        if (items.isEmpty()) {
            return null;
        } else {
            return parse(items, toValueType);
        }
    }

    protected Queue<Item> getItems(String fromValue) {
        return CucumberCore.instance().paramParser().generateItems(fromValue);
    }

    protected Object parsePumpkin(Queue<Item> itemQueue, @SuppressWarnings("unused") Type expectedType) {
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
        return res;
    }

    protected Object parseNonPumpkin(String fromValue, Type expectedType) {
        final JavaType javaType = objectMapper.constructType(expectedType);
        if (boolean.class.equals(expectedType) || Boolean.class.equals(expectedType)) {
            if (StringUtils.equalsAny(fromValue, "true","истина")) {
                return true;
            }
            if (StringUtils.equalsAny(fromValue, "false","ложь")) {
                return false;
            }
            return !Str.isBlank(fromValue);
        }
        if (javaType.isTypeOrSubTypeOf(List.class)) {
            return Arrays.stream((fromValue).split(delimiter))
                    .map(i -> objectMapper.convertValue(i, javaType.getContentType()))
                    .collect(Collectors.toList());
        }
        return objectMapper.convertValue(fromValue, javaType);
    }

    private Object parse(Queue<Item> itemQueue, Type expectedType) {
        Object res = parsePumpkin(itemQueue, expectedType);
        if (res instanceof String) {
            res = parseNonPumpkin((String) res, expectedType);
        }
        log.debug("[TRANSFORMER] Result : '{}'", res);
        return res;
    }
}