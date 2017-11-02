package ru.mk.pump.commons.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Waiter {

    public void sleep(int timeoutMS) {
        try {
            Thread.sleep(timeoutMS);
        } catch (InterruptedException ignore) {
        }
    }
}
