package ru.mk.pump.web.elements.internal;

import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ActionFactory {

    private final InternalElement element;

    protected ActionFactory(InternalElement element) {
        this.element = element;
    }

    public <R> Action<R> newAction(BiFunction<WebElement, Parameters, R> actionSupplier, String name) {
        return new AbstractAction<R>(actionSupplier, element, name) {
        };
    }

    public <R> Action<R> newAction(Function<WebElement, R> actionSupplier, String name) {
        return new AbstractAction<R>((webElement, param) -> actionSupplier.apply(webElement), element, name) {
        };
    }

    public Action<String> newAction(BiConsumer<WebElement, Parameters> actionConsumer, String name) {
        return new AbstractAction<String>((webElement, param) -> {
            actionConsumer.accept(webElement, param);
            return "void";
        }, element, name) {
        };
    }

    public Action<String> newAction(Consumer<WebElement> actionConsumer, String name) {
        return new AbstractAction<String>((webElement, param) -> {
            actionConsumer.accept(webElement);
            return "void";
        }, element, name) {
        };
    }
}
