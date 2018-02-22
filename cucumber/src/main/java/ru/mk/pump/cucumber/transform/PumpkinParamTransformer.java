package ru.mk.pump.cucumber.transform;

import cucumber.api.Transformer;
import cucumber.deps.com.thoughtworks.xstream.converters.SingleValueConverter;
import cucumber.runtime.ParameterInfo;
import cucumber.runtime.xstream.LocalizedXStreams;
import java.util.Locale;
import java.util.Queue;
import ru.mk.pump.cucumber.CucumberPumpCore;
import ru.mk.pump.web.interpretator.items.Item;

public class PumpkinParamTransformer extends Transformer<Object> {

    private final LocalizedXStreams nativeConverter = new LocalizedXStreams(getClass().getClassLoader());

    private ParameterInfo parameterInfo;

    @Override
    public Object transform(String value) {
        Queue<Item> items = CucumberPumpCore.instance().paramParser().generateItems(value);
        if (items.isEmpty()) {
            return null;
        } else {
            return parse(items);
        }
    }

    @Override
    public void setParameterInfoAndLocale(ParameterInfo parameterInfo, Locale locale) {
        super.setParameterInfoAndLocale(parameterInfo, locale);
        this.parameterInfo = parameterInfo;
    }

    private Object parse(Queue<Item> itemQueue) {
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
            SingleValueConverter converter = nativeConverter.get(getLocale()).getSingleValueConverter(parameterInfo.getType());
            if (converter != null) {
                return converter.fromString((String) res);
            }
        }
        return res;
    }
}