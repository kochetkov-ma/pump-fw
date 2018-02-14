package ru.mk.pump.web.common;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiFunction;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.exception.ExecutionException;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Preconditions;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.common.api.ItemsManager;
import ru.mk.pump.web.common.api.annotations.PAction;
import ru.mk.pump.web.common.api.annotations.PComponent;
import ru.mk.pump.web.common.api.annotations.PElement;
import ru.mk.pump.web.component.BaseComponent;
import ru.mk.pump.web.component.api.Component;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.interpretator.items.Field;
import ru.mk.pump.web.interpretator.items.Item;
import ru.mk.pump.web.interpretator.items.Method;
import ru.mk.pump.web.interpretator.items.TestParameter;
import ru.mk.pump.web.interpretator.rules.Pumpkin;
import ru.mk.pump.web.page.BasePage;
import ru.mk.pump.web.page.api.Page;
import ru.mk.pump.web.utils.TestVars;

@Slf4j
@ToString(exclude = {"pumpkin", "resultHandlerChain"})
@SuppressWarnings({"WeakerAccess", "unused"})
public class WebItemsController implements StrictInfo {

    public final static String VAR_LAST_RESULT = "controller_last_result";

    private final Set<BiFunction<Object, WebItemsController, Object>> resultHandlerChain = Sets.newHashSet();

    private final static BiFunction<Object, WebItemsController, Object> DEFAULT_HANDLER = (result, controller) -> {
        controller.getTestVars()
            .put(VAR_LAST_RESULT, result);
        return result;
    };

    @Getter
    private final ItemsManager<BasePage> pageManager;

    @Getter
    private final ItemsManager<BaseComponent> componentManager;

    private final Pumpkin pumpkin;

    @Getter
    private final TestVars testVars;

    @Getter
    private Object lastResult;

    private String lastPumpkinExpression;

    public WebItemsController(@NotNull ItemsManager<BasePage> pageManager, @NotNull ItemsManager<BaseComponent> componentManager, @NotNull Pumpkin pumpkin,
        @NotNull TestVars testVars) {
        this.pageManager = pageManager;
        this.componentManager = componentManager;
        this.pumpkin = pumpkin;
        this.testVars = testVars;
        resultHandlerChain.add(DEFAULT_HANDLER);
    }

    public WebItemsController(@NotNull ItemsManager<BasePage> pageManager, @NotNull ItemsManager<BaseComponent> componentManager,
        @NotNull TestVars testVars) {
        this(pageManager, componentManager, new Pumpkin(testVars.asMap()), testVars);
    }

    @NotNull
    public Component initComponent(@NotNull String pumpkinExpression) {
        Queue<Item> res = pumpkin.generateItems(pumpkinExpression);
        if (res.isEmpty()) {
            throw new IllegalArgumentException(String.format("Error pumpkin expression '%s' to creating Component", pumpkinExpression));
        }
        log.info("[CONTROLLER] Try to init current component from '{}'", res);
        return componentManager.getOne(cast(res.poll().getSource(), String.class));
    }

    @NotNull
    public Page initPage(@NotNull String pumpkinExpression) {
        Queue<Item> res = pumpkin.generateItems(pumpkinExpression);
        if (res.isEmpty()) {
            throw new IllegalArgumentException(String.format("Error pumpkin expression '%s' to creating Component", pumpkinExpression));
        }
        log.info("[CONTROLLER] Try to init current page from '{}'", res);
        return pageManager.getOne(cast(res.poll().getSource(), String.class));
    }

    @Nullable
    public Object execute(@NotNull String pumpkinExpression) {
        return execute(null, pumpkinExpression);
    }

    public Object executeOnCurrentPage(@NotNull String pumpkinExpression) {
        if (pageManager.getCurrent() == null) {
            throw new ExecutionException("Current page is undefined. Please, initPage before using")
                .addTarget("controller", this);
        }
        return execute(pageManager.getCurrent(), pumpkinExpression);
    }

    public Object executeOnCurrentComponent(@NotNull String pumpkinExpression) {
        if (componentManager.getCurrent() == null) {
            throw new ExecutionException("Current component is undefined. Please, initComponent before using")
                .addTarget("controller", this);
        }
        return execute(componentManager.getCurrent(), pumpkinExpression);
    }


    @Nullable
    public Object execute(@Nullable Object targetObject, @NotNull String pumpkinExpression) {
        this.lastPumpkinExpression = pumpkinExpression;
        Queue<Item> items = pumpkin.generateItems(pumpkinExpression);
        log.info("[CONTROLLER] Executing has started. Items are '{}'", items);
        log.info("[CONTROLLER] Target is present = '{}'", targetObject != null);
        lastResult = recurseExecute(targetObject, items);
        log.info("[CONTROLLER] Executing has finished. Result class is '{}'", getClass(lastResult));
        return walkOnAllHandlers(lastResult);
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoBuilder("WebItemsController")
            .put("page manager", Strings.toPrettyString(pageManager.getInfo()))
            .put("component manager", Strings.toPrettyString(componentManager.getInfo()))
            .put("last expression", lastPumpkinExpression)
            .put("lastResult", Strings.toString(lastResult))
            .put("test vars", testVars.toPrettyString())
            .build();
    }

    public WebItemsController clearResultHandlers() {
        resultHandlerChain.clear();
        resultHandlerChain.add(DEFAULT_HANDLER);
        return this;
    }

    public WebItemsController addResultHandler(BiFunction<Object, WebItemsController, Object> handler) {
        resultHandlerChain.add(handler);
        return this;
    }

    //region PRIVATE
    private Object walkOnAllHandlers(Object result) {
        return resultHandlerChain.stream().reduce(result, (res, handler) -> handler.apply(res, this), (prev, next) -> next);
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
        Preconditions.checkObjectNotNull(field, Field.class);
        if (candidate == null) {
            log.warn("[CONTROLLER] Target page or component is undefined");
            if (!pageManager.getList(field.getSource()).isEmpty()) {
                log.info("[CONTROLLER] Try to get target page '{}'", field.getSource());
                return pageManager.getCurrentList().get(0);
            } else {
                if (!componentManager.getList(field.getSource()).isEmpty()) {
                    log.info("[CONTROLLER] Try to get target component '{}'", field.getSource());
                    Component res = componentManager.getCurrentList().get(0);
                    res.initAllElements();
                    return res;
                } else {
                    throw new ExecutionException(
                        String.format("Target object is 'null' with field '%s'. Source expression is '%s'", Strings.toString(field), lastPumpkinExpression))
                        .addTarget("controller", this);
                }
            }
        }
        if (candidate instanceof Component) {
            return findField(candidate, field);
        }
        throw new ExecutionException(
            String.format("Object '%s' with field '%s' is not expected class : Page or Component. Source expression is '%s'", getClass(candidate),
                Strings.toString(field), lastPumpkinExpression))
            .addTarget("controller", this);
    }


    private String getClass(Object object) {
        return ClassUtils.getShortClassName(object, "null");
    }

    private Object findField(Object candidate, Field field) {
        Preconditions.checkObjectNotNull(candidate, Object.class);
        Preconditions.checkObjectNotNull(field, Method.class);
        if (candidate instanceof Component) {
            if (field.hasIndex()) {
                Object res = readField(candidate, field);
                if (!(res instanceof List)) {
                    throw new ExecutionException(
                        String.format("Target object '%s' with field '%s' is not expected class : List. Source expression is '%s'", getClass(candidate),
                            Strings.toString(field), lastPumpkinExpression))
                        .addTarget("controller", this);
                }
                return ((List) res).get(field.getIndex());
            } else {
                return readField(candidate, field);
            }
        }
        throw new ExecutionException(
            String.format("Target object '%s' with field '%s' is not expected class : Component. Source expression is '%s'", getClass(candidate),
                Strings.toString(field), lastPumpkinExpression))
            .addTarget("controller", this);
    }

    private Object readField(Object candidate, Field field) {
        Preconditions.checkObjectNotNull(candidate, Object.class,
            String.format("Target object with filed '%s' cannot be null . Source expression is '%s'", field, lastPumpkinExpression));
        java.lang.reflect.Field result;
        Optional<java.lang.reflect.Field> optionalField = FieldUtils.getFieldsListWithAnnotation(candidate.getClass(), PElement.class).stream()
            .filter(f -> f.getAnnotation(PElement.class).value().equalsIgnoreCase(field.getSource()) || f.getName().equalsIgnoreCase(field.getSource()))
            .findFirst();
        //noinspection OptionalIsPresent
        if (!optionalField.isPresent()) {
            result = FieldUtils.getFieldsListWithAnnotation(candidate.getClass(), PComponent.class).stream()
                .filter(f -> f.getAnnotation(PComponent.class).value().equalsIgnoreCase(field.getSource()) || f.getName().equalsIgnoreCase(field.getSource()))
                .findFirst()
                .orElseThrow(() -> new ExecutionException(
                    String.format("Cannot find in object '%s' field '%s'. Source expression is '%s'", getClass(candidate), Strings.toString(field),
                        lastPumpkinExpression))
                    .addTarget("controller", this));
        } else {
            result = optionalField.get();
        }
        result.setAccessible(true);
        try {
            return result.get(candidate);
        } catch (IllegalAccessException e) {
            throw new ExecutionException(String
                .format("Execution error in object '%s' field '%s'. Source expression is '%s'", getClass(candidate), Strings.toString(field),
                    lastPumpkinExpression), e)
                .addTarget("controller", this);
        }
    }

    private Object callMethod(Object candidate, Method method) {
        Preconditions.checkObjectNotNull(candidate, Object.class,
            String.format("Target object with method '%s' cannot be null. Source expression is '%s'", method, lastPumpkinExpression));
        Preconditions.checkObjectNotNull(method, Method.class, String.format("Method desc cannot be null. Source expression is '%s'", lastPumpkinExpression));
        if (candidate instanceof Page || candidate instanceof Component || candidate instanceof Element) {
            return invoke(candidate, method);
        }
        throw new ExecutionException(
            String.format("Object '%s' with method '%s' is not expected class : Page or Component or Element. Source expression is '%s'", getClass(candidate),
                Strings.toString(method), lastPumpkinExpression))
            .addTarget("controller", this);
    }

    private Object invoke(Object candidate, Method method) {
        Preconditions.checkObjectNotNull(candidate, Object.class);
        java.lang.reflect.Method oneMethod = Arrays.stream(candidate.getClass().getDeclaredMethods())
            .filter(m -> {
                if (m.isAnnotationPresent(PAction.class)) {
                    return m.getAnnotation(PAction.class).value().equalsIgnoreCase(method.getSource()) || m.getName().equalsIgnoreCase(method.getSource());
                } else {
                    return m.getName().equalsIgnoreCase(method.getSource());
                }
            })
            .filter(m -> m.getParameterCount() == method.getArgs().length)
            .findFirst()
            .orElseThrow(() -> new ExecutionException(
                String.format("Cannot find in object '%s' method '%s'", getClass(candidate), Strings.toString(method))).addTarget("controller", this)
                .addTarget("controller", this));
        try {
            oneMethod.setAccessible(true);
            return oneMethod.invoke(candidate, castArgs(oneMethod, method.getArgs()));
        } catch (Exception e) {
            throw new ExecutionException(String
                .format("Execution error in object '%s' method '%s'. Source expression is '%s'", getClass(candidate), Strings.toString(method),
                    lastPumpkinExpression), e)
                .addTarget("controller", this);
        }
    }

    private Object[] castArgs(java.lang.reflect.Method method, Object[] args) {
        final Object[] newArgs = new Object[args.length];
        Class<?>[] expectedArgs = method.getParameterTypes();
        for (int i = 0; i < expectedArgs.length; i++) {
            Preconditions.checkArgListSize(i, args.length, String.format("Methods arguments count must be '%s'", expectedArgs.length));
            if (String.class.isAssignableFrom(expectedArgs[i]) || !(args[i] instanceof String)) {
                newArgs[i] = args[i];
            } else {
                newArgs[i] = Strings.toObject(String.valueOf(args[i]), expectedArgs[i]);
            }
        }
        return newArgs;
    }
    //endregion

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