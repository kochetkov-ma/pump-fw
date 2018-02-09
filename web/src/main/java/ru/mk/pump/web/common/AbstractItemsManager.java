package ru.mk.pump.web.common;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.commons.utils.Preconditions;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.browsers.Browser;
import ru.mk.pump.web.common.api.ItemsManager;
import ru.mk.pump.web.common.api.WebObject;
import ru.mk.pump.web.exceptions.ItemManagerException;
import ru.mk.pump.web.utils.WebReporter;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString
abstract public class AbstractItemsManager<T extends WebObject> implements ItemsManager<T> {

    private final String[] packages;

    @Getter
    private final Set<Class<? extends T>> itemsSet;

    @Getter
    private final Browser browser;

    @Getter
    private final Reporter reporter;

    @Getter
    private T current;

    @Getter
    private List<T> currentList;

    //region CONSTRUCTORS
    public AbstractItemsManager(Browser browser, Reporter reporter, String... packagesName) {
        Preconditions.checkNotEmpty(packagesName);
        this.browser = browser;
        this.reporter = reporter;
        this.packages = packagesName;
        itemsSet = loadItemsClasses(packagesName);
    }

    public AbstractItemsManager(Browser browser, String... packagesName) {
        this(browser, WebReporter.getReporter(), packagesName);
    }
    //endregion

    @NotNull
    @Override
    public T getOne(String name) {
        return getOneByClass(name, getItemClass());
    }

    @NotNull
    @Override
    public List<T> getList(String name) {
        return getListByClass(name, getItemClass());
    }

    @NotNull
    @Override
    public <V extends T> Set<Class<V>> find(@NotNull String itemName, @NotNull Class<V> itemClass) {
        //noinspection unchecked
        Set<Class<V>> result = itemsSet.stream()
            .filter(itemClass::isAssignableFrom)
            .filter(i -> findFilter(i.getName(), i))
            .map(i -> (Class<V>) i)
            .collect(Collectors.toSet());
        if (result.isEmpty()) {
            throw new ItemManagerException(String.format("Cannot find any item with name '%s' and class '%s'", itemName, itemClass.getCanonicalName()))
                .withManager(this);
        } else {
            return result;
        }
    }

    @NotNull
    @Override
    public <V extends T> T getOneByClass(String name, Class<V> itemSubClass) {
        if (current.getName().equals(name)) {
            return current;
        }
        Set<Class<V>> items = find(name, itemSubClass);
        current = newItem(items.iterator().next());
        return current;
    }

    @NotNull
    @Override
    public <V extends T> List<T> getListByClass(String name, Class<V> itemSubClass) {
        Set<Class<V>> items = find(name, itemSubClass);
        currentList = items.stream().map(this::newItem).collect(Collectors.toList());
        return currentList;
    }

    protected T newItem(Class<? extends T> itemClass) {
        try {
            final Constructor<? extends T> constructor = findConstructor(itemClass);
            constructor.setAccessible(true);
            T result = newInstance(constructor);
            return afterItemCreate(result);
        } catch (ReflectiveOperationException | ClassCastException ex) {
            throw new ItemManagerException(String.format("Error when try to create item with class '%s'", getItemClass().getCanonicalName()), ex)
                .withManager(this);
        }
    }

    abstract protected T newInstance(Constructor<? extends T> constructor) throws ReflectiveOperationException;

    abstract protected Constructor<? extends T> findConstructor(Class<? extends T> itemClass) throws ReflectiveOperationException;

    abstract protected T afterItemCreate(T itemInstance);

    abstract protected boolean findFilter(String itemName, Class<? extends T> itemClass);

    abstract protected Class<T> getItemClass();

    private Set<Class<? extends T>> loadItemsClasses(String... packagesName) {
        final Reflections reflections = new Reflections((Object[]) packagesName);
        return reflections.getSubTypesOf(getItemClass());
    }

    @Override
    public Map<String, String> getInfo() {
        return StrictInfo.infoBuilder("Items Manager")
            .put("browser", Strings.toString(browser))
            .put("current item", Strings.toString(current))
            .put("current list", Strings.toPrettyString(currentList, "current list".length()))
            .put("reporter", Strings.toString(reporter))
            .put("loaded items", Strings.toPrettyString(itemsSet, "loaded items".length()))
            .put("packages", Strings.toPrettyString(packages, "packages".length()))
            .build();
    }
}