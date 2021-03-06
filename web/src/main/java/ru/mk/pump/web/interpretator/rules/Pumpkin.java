package ru.mk.pump.web.interpretator.rules;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Queues;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.exception.PumpException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.utils.Groovy;
import ru.mk.pump.commons.utils.Str;
import ru.mk.pump.web.interpretator.items.Field;
import ru.mk.pump.web.interpretator.items.Item;
import ru.mk.pump.web.interpretator.items.Method;
import ru.mk.pump.web.interpretator.items.TestParameter;

@SuppressWarnings({"unused", "WeakerAccess"})
@ToString
@Slf4j
public final class Pumpkin {

    private Groovy groovy;

    private final Set<Rule> rules;

    @Getter(AccessLevel.PROTECTED)
    private Deque<Item> itemsCache;

    @Getter(AccessLevel.PROTECTED)
    private Item currentItem;

    @Getter(AccessLevel.PROTECTED)
    private String currentExpression;

    @Getter(AccessLevel.PROTECTED)
    private PumpkinParser parser;

    private Map<String, Object> testVars;

    //region CONSTRUCTORS
    public Pumpkin(Map<String, Object> testVars) {
        this(testVars, Groovy.of());
    }

    public Pumpkin(Map<String, Object> testVars, Groovy groovy) {
        this(testVars, groovy, ImmutableSet
            .of(new EmptyArgumentRule(), new TestVarArgument(testVars), new GroovyArgumentRule(groovy), new StringArgumentRule(), new IndexRule(),
                new TitleRule()));
    }

    public Pumpkin(Map<String, Object> testVars, Groovy groovy, Set<Rule> rules) {
        this.groovy = groovy;
        this.rules = rules;
        this.testVars = testVars;
    }
    //endregion
    public static Pumpkin newParamParser(Map<String, Object> testVars) {
        Groovy groovy = Groovy.of();
        return new Pumpkin(testVars, groovy, ImmutableSet.of(new TestVarArgument(testVars), new GroovyArgumentRule(groovy), new StringRule()));
    }

    public Queue<Item> generateItems(@NonNull String expression) {
        this.currentExpression = expression;
        this.currentItem = null;
        this.itemsCache = Queues.newArrayDeque();
        this.parser = new PumpkinParser(expression, rules);

        while (!parser.isCompleted()) {
            Optional<Accumulator> accumulator = parser.parseNext();
            accumulator.ifPresent(this::handle);
        }
        if (itemsCache.isEmpty()) {
            itemsCache.add(new TestParameter<>(expression));
        }
        return itemsCache;
    }

    ///////////
    /*PRIVATE*/
    ///////////

    private void handle(Accumulator accumulator) {
        if (needNewItem(accumulator)) {
            currentItem = newItem(accumulator);
            itemsCache.add(currentItem);
        } else if (needModifyItem(accumulator)) {
            currentItem = modifyCurrentItem(accumulator);
        } else if (needParameterItem(accumulator)) {
            currentItem = newParameter(accumulator);
            itemsCache.add(currentItem);
        } else {
            throw exception("Unexpected rule", accumulator);
        }
    }

    private boolean needNewItem(Accumulator accumulator) {
        return accumulator.getRule() instanceof TitleRule;
    }

    private boolean needParameterItem(Accumulator accumulator) {
        return accumulator.getRule() instanceof SeparateValue;
    }

    private boolean needModifyItem(Accumulator accumulator) {
        return (currentItem instanceof Method || currentItem instanceof Field) && (accumulator.getRule() instanceof IndexRule ||
            accumulator.getRule() instanceof ArgumentRule);
    }

    private Item newParameter(Accumulator accumulator) {
        if (accumulator.getRule() instanceof SeparateValue) {
            return new TestParameter<>(accumulator.getRule().toValue(accumulator.getResult()));
        } else {
            throw exception("Unexpected rule", accumulator);
        }
    }

    /**
     * Генерация исключения, если текущее правило не подходит под создание элемента
     */
    private Item newItem(Accumulator accumulator) {
        if (accumulator.getRule() instanceof TitleRule) {
            return new Field(accumulator.getResult());
        } else {
            throw exception("Unexpected rule", accumulator);
        }
    }

    /**
     * Генерация исключения, если текущее правило не подходит под создание элемента
     */
    private Item modifyCurrentItem(Accumulator accumulator) {
        if (accumulator.getRule() instanceof ArgumentRule) {
            if (currentItem instanceof Field) {
                mutateCurrent(toMethod(currentItem));
            } else if (!((Method) currentItem).hasArgs()) {
                throw exception("New argument in non arguments method", accumulator);
            }
            if (!(accumulator.getRule() instanceof EmptyArgumentRule)) {
                ((Method) currentItem).addArg(accumulator.getRule().toValue(accumulator.getResult()));
            }
            return currentItem;
        } else if (accumulator.getRule() instanceof IndexRule) {
            if (currentItem instanceof Method) {
                //noinspection ConstantConditions
                mutateCurrent(toField(currentItem).setIndex(((IndexRule) accumulator.getRule()).toValue(accumulator.getResult())));
            } else if (((Field) currentItem).hasIndex()) {
                throw exception("Double index in a field", accumulator);
            }
            //noinspection ConstantConditions
            int res = ((IndexRule) accumulator.getRule()).toValue(accumulator.getResult());
            if (res < 0) {
                throw exception("Incorrect index", accumulator);
            }
            ((Field) currentItem).setIndex(res);
            return currentItem;
        }
        throw exception("Unexpected rule", accumulator);
    }

    private void mutateCurrent(Item item) {
        itemsCache.pollLast();
        itemsCache.add(item);
        currentItem = item;
    }

    private Method toMethod(Item item) {
        return new Method((String) item.getSource());
    }

    private Field toField(Item item) {
        return new Field((String) item.getSource());
    }

    private PumpException exception(String error, Accumulator accumulator) {
        throw new PumpException(new PumpMessage(error)
            .withPre("Error pampkin parsing")
            .withDesc(
                String.format("We have tried to parse you expression '%s'. But your expression is incorrect. See pumpkin documentation", currentExpression))
            .addExtraInfo("source", currentExpression)
            .addExtraInfo("current item", Str.toString(currentItem))
            .addExtraInfo("handled items", Str.toString(itemsCache))
            .addExtraInfo("parser", Str.toString(accumulator))
        );
    }

}
