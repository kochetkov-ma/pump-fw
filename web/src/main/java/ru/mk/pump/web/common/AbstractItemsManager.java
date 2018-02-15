package ru.mk.pump.web.common;

import com.google.common.collect.Sets;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
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
@ToString(exclude = {"browser", "reporter"})
//TODO : Продумать возможность выбора из нескольких вариантов странцы (мобильная и полная)
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
    private List<? extends T> currentList;
    private Set<BiPredicate<AbstractItemsManager<T>, Class<? extends T>>> predicateSet = Sets.newHashSet();

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

    @NonNull
    @Override
    public T getOne(String name) {
        return getOneByClass(name, getItemClass());
    }

    @NonNull
    @Override
    public List<T> getList(String name) {
        return getListByClass(name, getItemClass());
    }

    @NonNull
    @Override
    public <V extends T> Set<Class<V>> find(@NonNull String itemName, @NonNull Class<V> itemClass) {
        //noinspection unchecked
        return itemsSet.stream()
                .filter(itemClass::isAssignableFrom)
                .filter(i -> findFilter(itemName, i))
                .filter(i -> predicateSet.stream().allMatch(p -> p.test(this, i)))
                .map(i -> (Class<V>) i)
                .collect(Collectors.toSet());
    }

    /**
     * [RUS] Дополнительный фильтр для страниц. Например, если есть две версии одной страницы под разные браузеры или стенды.
     * Можно добавить к каждому классу страницы дополнительную аннотацию, а этим методом добавить правило проверки в зависимости от конфигурации
     */
    public AbstractItemsManager<T> addExtraFilter(BiPredicate<AbstractItemsManager<T>, Class<? extends T>> predicate) {
        this.predicateSet.add(predicate);
        return this;
    }

    public AbstractItemsManager<T> clearExtraFilter() {
        this.predicateSet.clear();
        return this;
    }

    @NonNull
    @Override
    public <V extends T> V getOneByClass(String name, Class<V> itemSubClass) {
        if (current != null && current.getName().equals(name) && current.getClass().isAssignableFrom(itemSubClass)) {
            //noinspection unchecked
            return (V) current;
        }
        Set<Class<V>> items = find(name, itemSubClass);
        if (items.isEmpty()) {
            throw new ItemManagerException(String.format("Cannot find any item with name '%s' and class '%s'", name, itemSubClass.getCanonicalName()))
                    .withManager(this);
        }
        current = newItem(items.iterator().next());
        //noinspection unchecked
        return (V) current;
    }

    @NonNull
    @Override
    public <V extends T> List<V> getListByClass(String name, Class<V> itemSubClass) {
        Set<Class<V>> items = find(name, itemSubClass);
        currentList = items.stream().map(this::newItem).collect(Collectors.toList());
        //noinspection unchecked
        return (List<V>) currentList;
    }

    protected T newItem(Class<? extends T> itemClass) {
        try {
            final Constructor<? extends T> constructor = findConstructor(itemClass);
            constructor.setAccessible(true);
            T result = newInstance(constructor, itemClass);
            return afterItemCreate(result);
        } catch (ReflectiveOperationException | ClassCastException ex) {
            throw new ItemManagerException(String.format("Error when try to create item with class '%s'", getItemClass().getCanonicalName()), ex)
                    .withManager(this);
        }
    }

    abstract protected T newInstance(Constructor<? extends T> constructor, Class<? extends T> itemClass) throws ReflectiveOperationException;

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
                .put("current list", Strings.toPrettyString(currentList))
                .put("reporter", Strings.toString(reporter))
                .put("loaded items", Strings.toPrettyString(itemsSet))
                .put("packages", Strings.toPrettyString(packages))
                .build();
    }
}