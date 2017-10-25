package ru.mk.pump.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class RandomUtilTest {

    @Test
    public void testNewRussianMobilePhone() {
        log.info(RandomUtil.newRussianMobilePhone("+7903"));
        log.info(RandomUtil.newRussianMobilePhone("7"));
        log.info(RandomUtil.newRussianMobilePhone("+"));
        log.info(RandomUtil.newRussianMobilePhone(""));
        log.info(RandomUtil.newRussianMobilePhone(null));
        log.info(RandomUtil.newRussianMobilePhone("+89307200000"));
        log.info(RandomUtil.newRussianMobilePhone("+8930720000"));
    }

    @Test
    public void testGetString() {
        log.info(RandomUtil.newRussianString(25));
        log.info(RandomUtil.newEnglishString(25));
        log.info(RandomUtil.newNumber(25));
    }
}