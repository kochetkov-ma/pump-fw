package ru.mk.pump.commons.utils;

import org.junit.Test;
import ru.mk.pump.commons.exception.UtilException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RandomUtilTest {

    @Test
    public void testNewRussianMobilePhone() {
        assertThat(RandomUtil.newRussianMobilePhone("+7903")).startsWith("+7903").hasSize(12);
        assertThat(RandomUtil.newRussianMobilePhone("7")).startsWith("+").hasSize(12);
        assertThat(RandomUtil.newRussianMobilePhone("+")).startsWith("+").hasSize(12);
        assertThat(RandomUtil.newRussianMobilePhone("")).startsWith("+7").hasSize(12);
        assertThat(RandomUtil.newRussianMobilePhone(null)).startsWith("+7").hasSize(12);
        assertThat(RandomUtil.newRussianMobilePhone("+89307200000")).startsWith("+89307200000").hasSize(12);
        assertThat(RandomUtil.newRussianMobilePhone("+8930720000")).startsWith("+8930720000").hasSize(12);

        assertThatThrownBy(() -> RandomUtil.newRussianMobilePhone("+893072000001"))
                .hasNoCause()
                .isInstanceOf(UtilException.class)
                .hasMessage("Cannot generate new phone for prefix size more 12 chars. Actual prefix +893072000001");

    }

    @Test
    public void testGetString() {
        assertThat(RandomUtil.newRussianString(25)).hasSize(25).matches("[а-яА-Я]{25}");
        assertThat(RandomUtil.newEnglishString(25)).hasSize(25).matches("[A-z]{25}");
        assertThat(RandomUtil.newNumber(25)).hasSize(25).matches("\\d{25}");
    }
}