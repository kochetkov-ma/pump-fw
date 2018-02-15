package ru.mk.pump.web.interpretator.rules;

import static org.assertj.core.api.Assertions.*;

import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.web.interpretator.items.Field;
import ru.mk.pump.web.interpretator.items.Item;
import ru.mk.pump.web.interpretator.items.Method;
import ru.mk.pump.web.interpretator.items.TestParameter;

@SuppressWarnings({"deprecation", "FieldCanBeLocal"})
@Slf4j
class PumpkinTest {

    private static Map<String, Object> vars;

    private static Pumpkin pumpkin;

    private String sourceExp;

    private Queue<Item> res;

    @BeforeAll
    static void beforeAll() {
        vars = ImmutableMap.of("var_1", 1, "empty_string", "");
    }

    @BeforeEach
    void before() {
        pumpkin = new Pumpkin(vars);
    }

    @Test
    void generateItems() {
        sourceExp = "methodOne().methodOne()";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new Method("methodOne"), new Method("methodOne"));

        sourceExp = "метод пусто( , ,)";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new Method("метод пусто").addArg(" ").addArg(" ").addArg(""));

        sourceExp = "метод(аргумент,аргумент,${var_1},$groovy{new Date().getDay()})";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new Method("метод").addArg("аргумент").addArg("аргумент").addArg(1).addArg(new Date().getDay()));

        sourceExp = "элемент один[1].метод один(аргумент один,${var_1},$groovy{new Date().getDay()}).поле.метод()";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res)
            .containsExactly(
                new Field("элемент один").setIndex(1),
                new Method("метод один").addArg("аргумент один").addArg(1).addArg(new Date().getDay()),
                new Field("поле"),
                new Method("метод")
            );

        sourceExp = "method one (args/,with/,esc)";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new Method("method one ").addArg("args,with,esc"));

        sourceExp = "fieldOne[0]";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new Field("fieldOne").setIndex(0));

        sourceExp = "simple_field";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new Field("simple_field"));

        sourceExp = "${var_1}";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new TestParameter<>(1));

        sourceExp = "$groovy{new Date().getDay()}";
        res = pumpkin.generateItems(sourceExp);
        log.info("{}", res);
        assertThat(res).containsExactly(new TestParameter<>(new Date().getDay()));

        sourceExp = "${}";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new TestParameter<>(null));

        sourceExp = "${not_exists}";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new TestParameter<>(null));

        sourceExp = "${empty_string}";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new TestParameter<>(""));

        sourceExp = "${empty_string}.${var_1}.simple_field";
        res = pumpkin.generateItems(sourceExp);
        assertThat(res).containsExactly(new TestParameter<>(""), new TestParameter<>(1), new Field("simple_field"));
    }

    @Test
    void getItemsCache() {
        sourceExp = "поле";
        pumpkin.generateItems(sourceExp);
        assertThat(pumpkin.getItemsCache()).containsExactly(new Field("поле"));
    }

    @Test
    void getCurrentItem() {
        sourceExp = "поле1.поле2";
        pumpkin.generateItems(sourceExp);
        log.info("{}", pumpkin);
        assertThat(pumpkin.getCurrentItem()).isEqualTo(new Field("поле2"));
    }

    @Test
    void getCurrentExpression() {
        sourceExp = "поле1.поле2";
        pumpkin.generateItems(sourceExp);
        assertThat(pumpkin.getCurrentExpression()).isEqualTo(sourceExp);
    }

    @Test
    void getParser() {
        assertThat(pumpkin.getParser()).isNull();
        pumpkin.generateItems("");
        assertThat(pumpkin.getParser()).isNotNull();
    }

    @Test
    void testToString() {
        sourceExp = "поле1.поле2";
        pumpkin.generateItems(sourceExp);
        log.info(pumpkin.toString());
        assertThat(pumpkin.toString()).isNotEmpty();
    }
}