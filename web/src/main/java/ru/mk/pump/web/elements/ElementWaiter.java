package ru.mk.pump.web.elements;

import java.util.concurrent.Callable;
import org.hamcrest.Matcher;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.commons.utils.Waiter.WaitResult;

public class ElementWaiter {

    private final int temeoutS;

    private final int delayMs;

    private final Waiter waiter;

    public ElementWaiter(int temeoutS, int delayMs) {

        this.temeoutS = temeoutS;
        this.delayMs = delayMs;
        this.waiter = new Waiter();
    }

    public ElementWaiter() {
        this(30, 0);
    }

    public <T> WaitResult<T> wait(Callable<T> supplier, Matcher<T> matcher) {
        final WaitResult<T> result = waiter.waitIgnoreExceptions(temeoutS, delayMs, supplier, matcher);
        waiter.clear();
        return result;
    }

    public WaitResult<Boolean> wait(Callable<Boolean> supplier) {
        final WaitResult<Boolean> result = waiter.waitIgnoreExceptions(temeoutS, delayMs, supplier);
        waiter.clear();
        return result;
    }
}
