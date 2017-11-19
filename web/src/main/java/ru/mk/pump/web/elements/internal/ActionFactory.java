package ru.mk.pump.web.elements.internal;

import org.openqa.selenium.WebElement;

import java.util.function.Consumer;
import java.util.function.Function;

public class ActionFactory {

    private final InternalElement element;

    public ActionFactory(InternalElement element) {

        this.element = element;
    }

    public <R> Action<R> newAction(Function<WebElement, R> actionSupplier, String name) {
        return new AbstractAction<R>(actionSupplier, element, name) {
        };
    }

    public Action<String> newAction(Consumer<WebElement> actionConsumer, String name) {
        return new AbstractAction<String>(webElement -> {
            actionConsumer.accept(webElement);
            return "void";
        }, element, name) {
        };
    }

}
