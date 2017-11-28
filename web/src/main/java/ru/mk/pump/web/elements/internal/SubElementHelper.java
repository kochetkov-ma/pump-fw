package ru.mk.pump.web.elements.internal;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.commons.utils.Waiter.WaitResult;
import ru.mk.pump.web.elements.ElementFactory;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.ElementConfig;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;
import ru.mk.pump.web.exceptions.ElementException;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SubElementHelper<T extends Element> {

    private final Class<T> subElementClass;

    private final AbstractElement<?> parent;

    private final ElementFactory elementFactory;

    private final ElementConfig elementConfig;

    protected SubElementHelper(Class<T> subElementClass, AbstractElement<?> parent, ElementFactory elementFactory) {
        this(subElementClass, parent, elementFactory, ElementConfig.of(Strings.space("sub-element of", parent.getName())));
    }

    protected SubElementHelper(Class<T> subElementClass, AbstractElement<?> parent, ElementFactory elementFactory, ElementConfig elementConfig) {
        this.subElementClass = subElementClass;
        this.parent = parent;
        this.elementFactory = elementFactory;
        this.elementConfig = elementConfig;
    }

    public T find(By by) {
        parent.getStateResolver().resolve(parent.exists()).result().throwExceptionOnFail((r) -> exceptionNoExists(r, by.toString()));
        final WebElement sourceWebElement = parent.getFinder().findFast().throwExceptionOnFail((r) -> exceptionNoExists(r, by.toString())).getResult();
        final List<WebElement> elements = sourceWebElement.findElements(by);
        if (elements.isEmpty()) {
            throw exceptionNoExistsSub(by.toString());
        }
        return elementFactory.newElement(subElementClass, by, elementConfig);

    }

    public List<T> findList(By by) {
        parent.getStateResolver().resolve(parent.exists()).result().throwExceptionOnFail((r) -> exceptionNoExists(r, by.toString()));
        final WebElement sourceWebElement = parent.getFinder().findFast().throwExceptionOnFail((r) -> exceptionNoExists(r, by.toString())).getResult();
        final List<WebElement> elements = sourceWebElement.findElements(by);
        if (elements.isEmpty()) {
            throw exceptionNoExistsSub(by.toString());
        }
        return IntStream.range(0, elements.size()).boxed()
            .map(index -> {
                final T newElement = elementFactory.newElement(subElementClass, by, elementConfig);
                ((InternalElement) newElement).setIndex(index);
                return newElement;
            })
            .collect(Collectors.toList());
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
    public List<T> findListXpathAdvanced(String xpathString, Predicate<WebElement> webElementPredicate, String... postfixXpaths) {
        final Pair<Integer, By> context = findXpath(xpathString, webElementPredicate, postfixXpaths);
        return IntStream.range(0, context.getKey()).boxed()
            .map(index -> {
                final T newElement = elementFactory.newElement(subElementClass, context.getValue(), elementConfig);
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
    public T findXpathAdvanced(String xpathString, Predicate<WebElement> webElementPredicate, String... postfixXpaths) {
        return elementFactory.newElement(subElementClass, findOneXpath(xpathString, webElementPredicate, postfixXpaths), elementConfig);
    }

    private By findOneXpath(String xpathString, Predicate<WebElement> webElementAttributePredicate, String... postfixXpaths) {
        if (webElementAttributePredicate == null) {
            webElementAttributePredicate = (el) -> true;
        }
        By byResult = By.xpath(xpathString);
        parent.getStateResolver().resolve(parent.exists()).result().throwExceptionOnFail((r) -> exceptionNoExists(r, "xpath: " + xpathString));
        final WebElement sourceWebElement = parent.getFinder().findFast().throwExceptionOnFail((r) -> exceptionNoExists(r, "xpath: " + xpathString))
            .getResult();
        final Iterator<String> iterator = Arrays.asList(postfixXpaths).iterator();
        List<WebElement> elements = sourceWebElement.findElements(byResult);
        while (elements.isEmpty() || !webElementAttributePredicate.test(elements.get(0))) {
            if (!iterator.hasNext()) {
                throw exceptionNoExistsSub("xpath: " + xpathString);
            }
            byResult = By.xpath(xpathString + iterator.next());
            elements = sourceWebElement.findElements(byResult);
        }
        return byResult;
    }

    private Pair<Integer, By> findXpath(String xpathString, Predicate<WebElement> webElementAttributePredicate, String... postfixXpaths) {
        if (webElementAttributePredicate == null) {
            webElementAttributePredicate = (el) -> true;
        }
        By byResult = By.xpath(xpathString);
        parent.getStateResolver().resolve(parent.exists()).result().throwExceptionOnFail((r) -> exceptionNoExists(r, "xpath: " + xpathString));
        final WebElement sourceWebElement = parent.getFinder().findFast().throwExceptionOnFail((r) -> exceptionNoExists(r, "xpath: " + xpathString))
            .getResult();
        final Iterator<String> iterator = Arrays.asList(postfixXpaths).iterator();
        List<WebElement> elements = sourceWebElement.findElements(byResult);
        while (elements.isEmpty() || !elements.stream().allMatch(webElementAttributePredicate)) {
            if (!iterator.hasNext()) {
                throw exceptionNoExistsSub("xpath: " + xpathString);
            }
            byResult = By.xpath(xpathString + iterator.next());
            elements = sourceWebElement.findElements(byResult);
        }
        return Pair.of(sourceWebElement.findElements(byResult).size(), byResult);
    }

    private ElementException exceptionNoExists(WaitResult<?> res, String byString) {
        return new ElementException(
            String.format("Cannot find sub elements '%s' by '%s' because parent is not exists", subElementClass.getSimpleName(), byString), parent,
            res.getCause());
    }

    private ElementException exceptionNoExistsSub(String byString) {
        return new ElementException(String.format("Cannot find any sub elements '%s' by '%s' ", subElementClass.getSimpleName(), byString), parent);
    }
}
