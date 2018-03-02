package ru.mk.pump.commons.helpers;

import static org.assertj.core.api.Assertions.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.utils.RandomUtil;
import ru.mk.pump.commons.utils.Strings;

class ParameterTest {

    private Item item;

    @BeforeEach
    void before() {
        item = newItem();
    }

    @Test
    void of() {
        Parameter<?> param = Parameter.of("name", Item.class, item, "random newItem");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", item)
            .hasFieldOrPropertyWithValue("stringValue", "random newItem");

        param = Parameter.of("", Item.class, null, null);
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", null)
            .hasFieldOrPropertyWithValue("stringValue", Strings.empty());

        param = Parameter.of("", String.class, null, "string");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "")
            .hasFieldOrPropertyWithValue("valueClass", String.class)
            .hasFieldOrPropertyWithValue("value", "string")
            .hasFieldOrPropertyWithValue("stringValue", "string");

        param = Parameter.of("", String.class, "", "string");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "")
            .hasFieldOrPropertyWithValue("valueClass", String.class)
            .hasFieldOrPropertyWithValue("value", "string")
            .hasFieldOrPropertyWithValue("stringValue", "string");

        param = Parameter.of("", String.class, "string", null);
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "")
            .hasFieldOrPropertyWithValue("valueClass", String.class)
            .hasFieldOrPropertyWithValue("value", "string")
            .hasFieldOrPropertyWithValue("stringValue", "string");

        param = Parameter.of("", String.class, "string", "");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "")
            .hasFieldOrPropertyWithValue("valueClass", String.class)
            .hasFieldOrPropertyWithValue("value", "string")
            .hasFieldOrPropertyWithValue("stringValue", "string");
    }

    @Test
    void of1() {
        Parameter<?> param = Parameter.of("name", Item.class, item);
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", item)
            .hasFieldOrPropertyWithValue("stringValue", item.toString());
    }

    @Test
    void of2() {
        Parameter<?> param = Parameter.of("name", Item.class);
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", null)
            .hasFieldOrPropertyWithValue("stringValue", "");
    }

    @Test
    void of3() {
        Parameter<String> param = Parameter.of("name", "");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", String.class)
            .hasFieldOrPropertyWithValue("value", "")
            .hasFieldOrPropertyWithValue("stringValue", "");
    }

    @Test
    void of4() {
        Parameter<String> param = Parameter.of("");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", StringConstants.UNDEFINED)
            .hasFieldOrPropertyWithValue("valueClass", String.class)
            .hasFieldOrPropertyWithValue("value", "")
            .hasFieldOrPropertyWithValue("stringValue", "");
        param = Parameter.of("string");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", StringConstants.UNDEFINED)
            .hasFieldOrPropertyWithValue("valueClass", String.class)
            .hasFieldOrPropertyWithValue("value", "string")
            .hasFieldOrPropertyWithValue("stringValue", "string");
    }

    @Test
    void of5() {
        Parameter<?> param = Parameter.of(Item.class, item);
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", StringConstants.UNDEFINED)
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", item)
            .hasFieldOrPropertyWithValue("stringValue", item.toString());
    }

    @Test
    void of6() {
        Parameter<?> param = Parameter.of("name", Item.class, "string");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", null)
            .hasFieldOrPropertyWithValue("stringValue", "string");

        param = Parameter.of("name", Item.class, "");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", null)
            .hasFieldOrPropertyWithValue("stringValue", "");
    }

    @Test
    void withName() {
        Parameter<?> param = Parameter.of(Item.class, item).withName("name");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", item)
            .hasFieldOrPropertyWithValue("stringValue", item.toString());
    }

    @Test
    void withValue() {
        Object obj = new Object();
        Parameter<?> paramA = Parameter.of("name", Item.class).withValue(obj);
        assertThat(paramA)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", obj)
            .hasFieldOrPropertyWithValue("stringValue", "");

        Parameter<String> param = Parameter.of("").withValue("string");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", StringConstants.UNDEFINED)
            .hasFieldOrPropertyWithValue("valueClass", String.class)
            .hasFieldOrPropertyWithValue("value", "string")
            .hasFieldOrPropertyWithValue("stringValue", "string");

        param = Parameter.of("").withValue(null);
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", StringConstants.UNDEFINED)
            .hasFieldOrPropertyWithValue("valueClass", String.class)
            .hasFieldOrPropertyWithValue("value", null)
            .hasFieldOrPropertyWithValue("stringValue", "");

        Parameter<?> paramO = Parameter.of("name", Item.class).withValue(item);
        assertThat(paramO)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", item)
            .hasFieldOrPropertyWithValue("stringValue", "");


    }

    @Test
    void withStringValue() {
        Parameter<?> param = Parameter.of("name", Item.class).withStringValue("string");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", null)
            .hasFieldOrPropertyWithValue("stringValue", "string");

        param = Parameter.of("name", Item.class).withStringValue(null);
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "name")
            .hasFieldOrPropertyWithValue("valueClass", Item.class)
            .hasFieldOrPropertyWithValue("value", null)
            .hasFieldOrPropertyWithValue("stringValue", "");
    }

    @Test
    void getValue() {
        final Parameter<?> param = Parameter.of("name", Item.class, item, "random newItem");
        assertThat(param.getValue()).isEqualTo(item);
        assertThat(param.getValue(Item.class)).isEqualTo(item);
        assertThat(param.getValue(Object.class)).isEqualTo(item);
        assertThatThrownBy(() -> param.getValue(String.class)).isInstanceOf(IllegalArgumentException.class);

        final Parameter<?> paramA = Parameter.of("name", Item.class, null, "random newItem");
        assertThat(paramA.getValue()).isNull();
        assertThat(paramA.getValue(Item.class)).isNull();
        assertThat(paramA.getValue(Object.class)).isNull();
        assertThat(paramA.getValue(String.class)).isNull();
    }

    @Test
    void checkClass() {
        final Parameter<?> param = Parameter.of("name", Item.class, item, "random newItem");
        assertThatCode(() -> param.checkClass(Item.class)).doesNotThrowAnyException();
        assertThatCode(() -> param.checkClass(Object.class)).doesNotThrowAnyException();
        assertThatThrownBy(() -> param.checkClass(String.class)).isInstanceOf(IllegalArgumentException.class);

        final Parameter<?> paramA = Parameter.of("name", Item.class, null, "random newItem");
        assertThatCode(() -> paramA.checkClass(Item.class)).doesNotThrowAnyException();
        assertThatCode(() -> paramA.checkClass(Object.class)).doesNotThrowAnyException();
        assertThatCode(() -> paramA.checkClass(String.class)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getName() {
        Parameter<?> param = Parameter.of("name", Item.class, item, "random newItem");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "name");

        param = Parameter.of("", Item.class, item, "random newItem");
        assertThat(param)
            .hasFieldOrPropertyWithValue("name", "");
        assertThat(Parameter.of("string"))
            .hasFieldOrPropertyWithValue("name", StringConstants.UNDEFINED);
    }

    @Test
    void equals() {
        Parameter<?> param1 = Parameter.of("name", Item.class, item, "random newItem");
        Parameter<?> param2 = Parameter.of("name", Item.class, item, "random newItem");
        assertThat(param1).isEqualTo(param2);
    }

    @Test
    void testToString() {
        Parameter<?> param1 = Parameter.of("name", Item.class, item, "random newItem");
        assertThat(param1.toString())
            .contains("name")
            .contains("valueClass")
            .contains("value")
            .contains("stringValue");
    }

    @ToString
    @Getter
    @EqualsAndHashCode
    private static class Item {

        private final String value;

        private final String key;

        private Item() {
            this.value = RandomUtil.newEnglishString(5);
            this.key = RandomUtil.newEnglishString(5);
        }
    }

    private Item newItem() {
        return new Item();
    }
}