package ru.mk.pump.web.exceptions;

import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

import java.util.function.Consumer;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ElementException extends PumpException {

    private static final String TITLE = "Pump element error";

    public ElementException(String message) {
        super(message);
    }

    public ElementException(InternalElement element) {
        super(message(null, element, null));
    }

    public ElementException(InternalElement element, Consumer<PumpMessage> pumpMessageConsumer) {
        super(message(null, element, pumpMessageConsumer));
    }

    public ElementException(InternalElement element, Throwable throwable) {
        super(message(null, element, null), throwable);
    }

    public ElementException(InternalElement element, Consumer<PumpMessage> pumpMessageConsume, Throwable throwable) {
        super(message(null, element, pumpMessageConsume), throwable);
    }

    public ElementException(String message, InternalElement element) {
        super(message(message, element, null));
    }

    public ElementException(String message, InternalElement element, Consumer<PumpMessage> pumpMessageConsumer) {
        super(message(message, element, pumpMessageConsumer));
    }

    public ElementException(String message, InternalElement element, Throwable throwable) {
        super(message(message, element, null), throwable);
    }

    public ElementException(String message, InternalElement element, Consumer<PumpMessage> pumpMessageConsume, Throwable throwable) {
        super(message(message, element, pumpMessageConsume), throwable);
    }

    private static PumpMessage message(String desc, InternalElement element, Consumer<PumpMessage> pumpMessageConsumer) {
        final PumpMessage res = new PumpMessage(TITLE).withDesc(desc)
                .addExtraInfo("type", element.getClass().getSimpleName())
                .addExtraInfo("is list", String.valueOf(element.isList()))
                .addExtraInfo("name", element.getName())
                .addExtraInfo("by", element.getBy().toString())
                .addEnvInfo("browser id", element.getBrowser().getId())
                .addEnvInfo("browser config", element.getBrowser().toString());
        if (pumpMessageConsumer != null) {
            pumpMessageConsumer.accept(res);
        }
        return res;
    }
}
