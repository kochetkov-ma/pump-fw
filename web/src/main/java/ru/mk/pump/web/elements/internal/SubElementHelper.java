package ru.mk.pump.web.elements.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.ElementConfig;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.elements.utils.Xpath;
import ru.mk.pump.web.exceptions.ElementException;
import ru.mk.pump.web.exceptions.ElementFinderNotFoundException;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SubElementHelper<T extends Element> {

    private final Class<T> subElementClass;

    private final BaseElement parent;

    private final ElementFactory elementFactory;

    private final ElementConfig elementConfig;

    protected SubElementHelper(Class<T> subElementClass, BaseElement parent, ElementFactory elementFactory) {
        this(subElementClass, parent, elementFactory,
            ElementConfig.of(Strings.space("sub-element of", parent.getName()), Strings.space("sub-element of", parent.getDescription())));
    }

    protected SubElementHelper(Class<T> subElementClass, BaseElement parent, ElementFactory elementFactory, ElementConfig elementConfig) {
        this.subElementClass = subElementClass;
        this.parent = parent;
        this.elementFactory = elementFactory;
        this.elementConfig = elementConfig;
    }

    public SubElementHelper<T> newWithElementConfig(ElementConfig elementConfig) {
        return new SubElementHelper<>(subElementClass, parent, elementFactory, elementConfig);
    }

    public T find(@NotNull By... bys) {
        final List<T> list = findList(bys);
        if (list.isEmpty()) {
            throw exceptionNoExistsSub(Arrays.toString(bys));
        }
        return list.get(0);

    }

    public List<T> findList(@NotNull Predicate<List<WebElement>> webElementListPredicate, @NotNull By... bys) {
        if (bys.length == 0) {
            return Collections.emptyList();
        }
        for (By currentBy : bys) {
            parent.getStateResolver().resolve(parent.jsReady()).result().throwExceptionOnFail((r) -> exceptionNoExists(r, currentBy.toString()));
            parent.getStateResolver().resolve(parent.exists()).result().throwExceptionOnFail((r) -> exceptionNoExists(r, currentBy.toString()));
            final WebElement sourceWebElement = parent.getFinder().findFast().throwExceptionOnFail((r) -> exceptionNoExists(r, currentBy.toString()))
                .getResult();
            final List<WebElement> elements = sourceWebElement.findElements(Xpath.fixIfXpath(currentBy));
            if (webElementListPredicate.test(elements)) {
                return IntStream.range(0, elements.size()).boxed()
                    .map(index -> {
                        final T newElement = elementFactory.newElement(subElementClass, Xpath.fixIfXpath(currentBy), parent, elementConfig);
                        ((InternalElement) newElement).setIndex(index);
                        return newElement;
                    })
                    .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    public List<T> findList(@NotNull By... bys) {
        return findList((els) -> !els.isEmpty(), bys);
    }


    /**
     * [RUS]
     * Продвинутый поиск элементов
     * Получить набор элементов выбранного класса, все элементы соответствуют переданному предикату и одному из возможных xpath путей
     * <ul>
     *  <li/>сначала ищются элементы по xpathString и каждый элемент проверяется предикатом
     *  <li/>если хотябы один из найденных элементов не соответствует предикату, то к исходному xpath добавлется postfixXpath
     *  <li/>ищются элементы по xpathString + postfixXpath и каждый элемент проверяется предикатом
     *  <li/>предыдущий шаг выполняется для каждого postfixXpath
     *  <li/>если не найдено списка элементов, каждый элемент которого проверен предикатом, то Исключение ElementException
     * </ul>
     *
     * @param xpathString главный xpath. Если по нему однозначно найден список элементов, КАЖДЫЙ из которых соответствует предикату, то дополнительные postfixXpaths не будут задействованы
     * @param webElementPredicate предикат, которому должен соответствовать КАЖДЫЙ элемент, найденный по вариантам xpath (т.е. используется не filter(), а allMatch())
     * @param postfixXpaths массив дополнительных xpath, которые соединяются с основным xpathString
     * @throws ElementException если не найдено списка элементов, соответствующего условию
     * @return Полностью готовый список элементов, созданный с помощью ElementFactory.
     */
    public List<T> findListXpathAdvanced(@Nullable String xpathString, @Nullable Predicate<WebElement> webElementPredicate, @Nullable String... postfixXpaths) {
        final Pair<Integer, By> context = findXpath(xpathString, webElementPredicate, false, postfixXpaths);
        if (context.getKey() < 1) {
            return Collections.emptyList();
        }
        return IntStream.range(0, context.getKey()).boxed()
            .map(index -> {
                final T newElement = elementFactory.newElement(subElementClass, context.getValue(), parent, elementConfig);
                ((InternalElement) newElement).setIndex(index);
                return newElement;
            })
            .collect(Collectors.toList());
    }

    /**
     * [RUS]
     * Продвинутый поиск элемента
     * Получить элемент выбранного класса который соответствуют переданному предикату и одному из возможных xpath путей
     * <ul>
     *  <li/>сначала ищются элементы по xpathString и ПЕРВЫЙ найденный проверяется предикатом
     *  <li/>если ПЕРВЫЙ элемент не соответствует предикату, то к исходному xpath добавлется postfixXpath
     *  <li/>ищются элементы по xpathString + postfixXpath и каждый элемент проверяется предикатом
     *  <li/>предыдущий шаг выполняется для каждого postfixXpath из переданного массива
     *  <li/>если не найдено элемента, который проверен предикатом, то Исключение ElementException
     * </ul>
     *
     * @param xpathString главный xpath. Если по нему однозначно найден элемент, который соответствует предикату, то дополнительные postfixXpaths не будут задействованы
     * @param webElementPredicate предикат, которому должен соответствовать ПЕРВЫЙ элемент, найденный по вариантам xpath (т.е. используется не test(elements.get(0)))
     * @param postfixXpaths массив дополнительных xpath, которые соединяются с основным xpathString
     * @throws ElementException если не найдено элемента, соответствующего условию
     * @return Полностью готовый элемент, созданный с помощью ElementFactory.
     */
    public T findXpathAdvanced(@Nullable String xpathString, @Nullable Predicate<WebElement> webElementPredicate, @Nullable String... postfixXpaths) {
        final Pair<Integer, By> context = findXpath(xpathString, webElementPredicate, true, postfixXpaths);
        if (context.getKey() < 1) {
            throw exceptionNoExistsSub(xpathString + " " + Strings.toString(postfixXpaths));
        }
        return elementFactory.newElement(subElementClass, context.getValue(), parent, elementConfig);
    }

    private Pair<Integer, By> findXpath(String xpathString, Predicate<WebElement> webElementAttributePredicate, boolean one, String... postfixXpaths) {
        if (webElementAttributePredicate == null) {
            webElementAttributePredicate = (el) -> true;
        }
        By byResult;
        final String xpathFinal;

        xpathFinal = Xpath.fixXpath(xpathString);
        byResult = By.xpath(xpathFinal);

        parent.getStateResolver().resolve(parent.jsReady()).result().throwExceptionOnFail((r) -> exceptionNoExists(r, "xpath: " + xpathString));
        parent.getStateResolver().resolve(parent.exists()).result().throwExceptionOnFail((r) -> exceptionNoExists(r, "xpath: " + xpathString));
        final WebElement sourceWebElement = parent.getFinder().findFast().throwExceptionOnFail((r) -> exceptionNoExists(r, "xpath: " + xpathString))
            .getResult();
        final Iterator<String> iterator;
        if (postfixXpaths == null) {
            iterator = Collections.emptyIterator();
        } else {
            iterator = Arrays.asList(postfixXpaths).iterator();
        }
        List<WebElement> elements = sourceWebElement.findElements(byResult);
        while (iterator.hasNext() && checkPredicate(elements, webElementAttributePredicate, one)) {
            byResult = By.xpath(Xpath.concat(xpathFinal, iterator.next()));
            elements = sourceWebElement.findElements(byResult);
        }
        return Pair.of(sourceWebElement.findElements(byResult).size(), byResult);
    }

    private boolean checkPredicate(List<WebElement> webElements, Predicate<WebElement> webElementAttributePredicate, boolean one) {
        if (one) {
            return webElements.isEmpty() || !webElementAttributePredicate.test(webElements.get(0));
        } else {
            return webElements.isEmpty() || !webElements.stream().allMatch(webElementAttributePredicate);
        }
    }

    private ElementException exceptionNoExists(WaitResult<?> res, String byString) {
        return new ElementFinderNotFoundException(
            String.format("Cannot find sub elements '%s' by '%s' because parent is not exists", subElementClass.getSimpleName(), logPath(byString)),
            res.getCause())
            .withTargetElement(parent);
    }

    private ElementException exceptionNoExistsSub(String byString) {
        return new ElementFinderNotFoundException(
            String.format("Cannot find any sub elements '%s' by '%s' ", subElementClass.getSimpleName(), logPath(byString)))
            .withTargetElement(parent);
    }

    private String logPath(String byString) {
        return "parent[" + parent.getBy().toString() + "] " + byString;
    }
}
