package ru.mk.pump.web.elements.internal;

import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.helpers.Parameters;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

import java.util.function.*;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ActionFactory {

    private final InternalElement element;

    protected ActionFactory(InternalElement element) {
        this.element = element;
    }

    public <R> Action<R> newAction(BiFunction<WebElement, Parameters, R> actionBiFunction, String name) {
        return new AbstractAction<R>(actionBiFunction, element, name) {
        };
    }

    public <R> Action<R> newAction(Function<WebElement, R> actionSupplier, String name) {
        return new AbstractAction<R>((webElement, param) -> actionSupplier.apply(webElement), element, name) {
        };
    }

    public <R> Action<R> newAction(Supplier<R> actionSupplier, String name) {
        return new AbstractAction<R>((webElement, param) -> actionSupplier.get(), element, name) {
        };
    }

    public Action newVoidAction(BiConsumer<WebElement, Parameters> actionConsumer, String name) {
        return new AbstractAction<String>((webElement, param) -> {
            actionConsumer.accept(webElement, param);
            return "void";
        }, element, name) {
        };
    }

    public Action newVoidAction(Consumer<WebElement> actionConsumer, String name) {
        return new AbstractAction<String>((webElement, param) -> {
            actionConsumer.accept(webElement);
            return "void";
        }, element, name) {
        };
    }

    public Action newVoidAction(Runnable actionRunnable, String name) {
        return new AbstractAction<String>((webElement, param) -> {
            actionRunnable.run();
            return "void";
        }, element, name) {
        };
    }
}
