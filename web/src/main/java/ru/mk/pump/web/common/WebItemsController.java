package ru.mk.pump.web.common;

import java.lang.reflect.InvocationTargetException;
import java.util.Queue;
import lombok.Getter;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.exception.ExecutionException;
import ru.mk.pump.commons.utils.Preconditions;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.common.api.ItemsManager;
import ru.mk.pump.web.common.api.annotations.PAction;
import ru.mk.pump.web.component.BaseComponent;
import ru.mk.pump.web.component.api.Component;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.interpretator.items.Field;
import ru.mk.pump.web.interpretator.items.Item;
import ru.mk.pump.web.interpretator.items.Method;
import ru.mk.pump.web.interpretator.items.TestParameter;
import ru.mk.pump.web.interpretator.rules.Pumpkin;
import ru.mk.pump.web.page.BasePage;
import ru.mk.pump.web.page.api.Page;

@SuppressWarnings({"WeakerAccess", "unused"})
public class WebItemsController {

    @Getter
    private final ItemsManager<BasePage> pageManager;

    @Getter
    private final ItemsManager<BaseComponent> componentManager;

    private final Pumpkin pumpkin;

    private BaseElement lastElement;

    public WebItemsController(ItemsManager<BasePage> pageManager, ItemsManager<BaseComponent> componentManager, Pumpkin pumpkin) {
        this.pageManager = pageManager;
        this.componentManager = componentManager;
        this.pumpkin = pumpkin;
    }

    public Component initComponent(String pumpkinExpression) {
        Queue<Item> res = pumpkin.generateItems(pumpkinExpression);
        if (res.isEmpty()) {
            throw new IllegalArgumentException(String.format("Error pumpkin expression '%s' to creating Component", pumpkinExpression));
        }
        return componentManager.getOne(cast(res.iterator().next().getSource(), String.class));
    }

    public Page initPage(String pumpkinExpression) {
        Queue<Item> res = pumpkin.generateItems(pumpkinExpression);
        if (res.isEmpty()) {
            throw new IllegalArgumentException(String.format("Error pumpkin expression '%s' to creating Component", pumpkinExpression));
        }
        return pageManager.getOne(cast(res.iterator().next().getSource(), String.class));
    }

    public Object execute(String pumpkinExpression) {
        return execute(null, pumpkinExpression);
    }

    public Object execute(Object targetObject, String pumpkinExpression) {
        return recurseExecute(targetObject, pumpkin.generateItems(pumpkinExpression));
    }

    private Object recurseExecute(Object prev, Queue<Item> pumpkinItems) {
        if (pumpkinItems.isEmpty()) {
            return prev;
        }
        Item item = pumpkinItems.poll();
        if (item instanceof TestParameter) {
            return recurseExecute(item.getSource(), pumpkinItems);
        }
        if (item instanceof Method) {
            return recurseExecute(callMethod(prev, (Method) item), pumpkinItems);
        }
        if (item instanceof Field) {
            return recurseExecute(getField(prev, ((Field) item)), pumpkinItems);
        }
        throw new UnknownError("It should not have happened");
    }

    private Object getField(Object candidate, Field field) {
        Preconditions.checkObjectNotNull(candidate, Object.class);
        Preconditions.checkObjectNotNull(field, Field.class);
        if (candidate instanceof Page) {
            if (field.hasIndex()) {
                return pageManager.getList(field.getSource()).get(field.getIndex());
            } else {
                return pageManager.getOne(field.getSource());
            }
        }
        if (candidate instanceof Component) {
            if (field.hasIndex()) {
                return componentManager.getList(field.getSource()).get(field.getIndex());
            } else {
                return componentManager.getOne(field.getSource());
            }
        }
        throw new IllegalArgumentException(
            String.format("Object '%s' with field '%s' is not expected class : Page or Component", candidate.getClass(), Strings.toString(field)));
    }

    private Object callMethod(Object candidate, Method method) {
        Preconditions.checkObjectNotNull(candidate, Object.class);
        Preconditions.checkObjectNotNull(method, Method.class);
        if (candidate instanceof Page || candidate instanceof Component || candidate instanceof Element) {
            return invoke(candidate, method);
        }
        throw new IllegalArgumentException(
            String.format("Object '%s' with method '%s' is not expected class : Page or Component or Element", candidate.getClass(), Strings.toString(method)));
    }

    private Object invoke(Object candidate, Method method) {
        java.lang.reflect.Method oneMethod = MethodUtils.getMethodsListWithAnnotation(candidate.getClass(), PAction.class, true, true).stream()
            .filter(m -> m.getAnnotation(PAction.class).value().equalsIgnoreCase(method.getSource()) || m.getName().equalsIgnoreCase(method.getSource()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Cannot find in object '%s' method '%s'", candidate.getClass(), Strings.toString(method))));
        try {
            return oneMethod.invoke(candidate, method.getArgs());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ExecutionException(String.format("Execution error in object '%s' method '%s'", candidate.getClass(), Strings.toString(method)), e);
        }
    }

    @Nullable
    public static <T> T cast(@Nullable Object object, @NotNull Class<T> expectedClass) {
        if (object == null) {
            return null;
        }
        if (expectedClass.isAssignableFrom(object.getClass())) {
            //noinspection unchecked
            return (T) object;
        } else {
            throw new IllegalArgumentException(
                String.format("Object '%s' is not assignable from expected class '%s'", Strings.toString(object), expectedClass.getCanonicalName()));
        }

    }
}