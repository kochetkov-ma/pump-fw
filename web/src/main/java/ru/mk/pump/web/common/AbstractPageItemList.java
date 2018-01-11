package ru.mk.pump.web.common;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.AbstractList;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.common.api.PageItem;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.api.ElementConfig;
import ru.mk.pump.web.elements.internal.BaseElement;
import ru.mk.pump.web.elements.utils.Xpath;
import ru.mk.pump.web.exceptions.ElementException;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;

@SuppressWarnings({"WeakerAccess", "unused"})
@ToString
public abstract class AbstractPageItemList<T extends PageItem> extends AbstractList<T> {

    private TIntObjectMap<T> elementsCache = new TIntObjectHashMap<>();

    protected final BaseElement parent;

    protected final ElementFactory itemFactory;

    protected final Class<T> itemsClass;

    protected final By listBy;

    protected final ElementConfig elementConfig;

    public AbstractPageItemList(@NotNull Class<T> itemsClass, @NotNull By listBy, @NotNull ElementFactory elementFactory,
        @NotNull ElementConfig itemFactory) {
        this(itemsClass, listBy, null, elementFactory, itemFactory);
    }

    public AbstractPageItemList(@NotNull Class<T> itemsClass, @NotNull By listBy, @Nullable BaseElement parent, @NotNull ElementFactory itemFactory,
        @NotNull ElementConfig elementConfig) {
        super();
        this.itemsClass = itemsClass;
        this.listBy = Xpath.fixIfXpath(listBy);
        this.parent = parent;
        this.itemFactory = itemFactory;
        this.elementConfig = elementConfig;
    }

    abstract public T get(int index);

    /**
     * [RUS] Проверяет выполнение JS, проверяет существование родительского элемента, выполняет {@link org.openqa.selenium.WebDriver#findElements(By)}
     * и возвращает кол-во найденных элементов. Если родитель не задан, то выполняется поиск по всей странице
     */
    @Override
    public int size() {
        if (parent != null) {
            parent.getStateResolver().resolve(parent.jsReady()).result().throwExceptionOnFail((r) -> exceptionNoExists(r, listBy.toString()));
            parent.getStateResolver().resolve(parent.exists()).result().throwExceptionOnFail((r) -> exceptionNoExists(r, listBy.toString()));
            final WebElement sourceWebElement = parent.getFinder().findFast().throwExceptionOnFail((r) -> exceptionNoExists(r, listBy.toString()))
                .getResult();
            return sourceWebElement.findElements(listBy).size();
        } else {
            return itemFactory.getBrowser().getDriver().findElements(listBy).size();
        }
    }

    @Override
    public T remove(int index) {
        return elementsCache.remove(index);
    }

    protected T getCache(int index) {
        return elementsCache.get(index);
    }

    protected T saveCache(int index, T item) {
        elementsCache.put(index, item);
        return item;
    }

    protected boolean hasCache(int index) {
        return elementsCache.containsKey(index);
    }

    //region PRIVATE
    private ElementException exceptionNoExists(WaitResult<?> res, String byString) {
        return new ElementFinderNotFoundException(
            String.format("Cannot find sub elements '%s' by '%s' because parent is not exists", itemsClass.getSimpleName(), logPath(byString)),
            res.getCause())
            .withTargetElement(parent);
    }

    private String logPath(String byString) {
        return "parent[" + parent.getBy().toString() + "] " + byString;
    }
    //endregion
}