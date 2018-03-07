package ru.mk.pump.commons.utils;


import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.test.TestClassOne;
import ru.mk.pump.commons.test.TestClassTwo;
import ru.mk.pump.commons.test.one.TestClassThree;
import ru.mk.pump.commons.test.one.TestClassThree.Inner;
import ru.mk.pump.commons.test.two.TestClassFour;

@Slf4j
class ReflectionUtilsTest {

    @Test
    void getAllClasses() {
        assertThat(ReflectionUtils.getAllClasses("ru.mk.pump.commons.test"))
            .containsExactlyInAnyOrder(TestClassTwo.class, TestClassOne.class, TestClassThree.class, TestClassFour.class, Inner.class);
    }

    @Test
    void getAllClasses1() {
        assertThat(ReflectionUtils.getAllClasses(null, "ru.mk.pump.commons.test"))
            .containsExactlyInAnyOrder(TestClassTwo.class, TestClassOne.class, TestClassThree.class, TestClassFour.class, Inner.class);

        assertThat(ReflectionUtils.getAllClasses(Object.class, "ru.mk.pump.commons.test"))
            .containsExactlyInAnyOrder(TestClassTwo.class, TestClassOne.class, TestClassThree.class, TestClassFour.class, Inner.class);

        assertThat(ReflectionUtils.getAllClasses(Number.class, "ru.mk.pump.commons.test"))
            .containsExactlyInAnyOrder(TestClassTwo.class);

        assertThat(ReflectionUtils.getAllClasses(Thread.class, "ru.mk.pump.commons.test"))
            .containsExactlyInAnyOrder(Inner.class);
    }

    @Test
    void getAllClasses2() {
        assertThat(ReflectionUtils.getAllClasses(null, "ru.mk.pump.commons.test.one", "ru.mk.pump.commons.test.two"))
            .containsExactlyInAnyOrder(TestClassThree.class, TestClassFour.class, Inner.class);

        assertThat(ReflectionUtils.getAllClasses(Thread.class, "ru.mk.pump.commons.test.one", "ru.mk.pump.commons.test.two"))
            .containsExactlyInAnyOrder(Inner.class);
    }
}