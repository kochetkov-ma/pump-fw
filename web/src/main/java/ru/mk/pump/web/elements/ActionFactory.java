package ru.mk.pump.web.elements;

import java.util.function.Supplier;
import ru.mk.pump.web.elements.internal.InternalElement;

public class ActionFactory {

    private final InternalElement element;

    public ActionFactory(InternalElement element) {

        this.element = element;
    }

    public <T> Action<T> newAction(Supplier<T> actionSupplier, String name) {
        return new AbstractAction<T>(actionSupplier, element, name) {
        };
    }

    public Action<String> newAction(Runnable actionRunnable, String name) {
        return new AbstractAction<String>(() -> {
            actionRunnable.run();
            return "void";
        }, element, name) {
        };
    }

}
