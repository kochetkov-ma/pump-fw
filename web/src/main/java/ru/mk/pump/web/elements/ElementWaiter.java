package ru.mk.pump.web.elements;

import java.util.concurrent.Callable;
import lombok.Getter;
import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.commons.utils.Waiter.WaitResult;

public class ElementWaiter {

    @Getter
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

    public ElementWaiter clear() {
        waiter.clear();
        return this;
    }

    public ElementWaiter withNotIgnoreExceptions(@NotNull Class<? extends Throwable> notIgnoringThrowable) {
        waiter.withNotIgnoreExceptions(notIgnoringThrowable);
        return this;
    }

    public <T> WaitResult<T> wait(Callable<T> supplier, Matcher<T> matcher) {
        return waiter.waitIgnoreExceptions(temeoutS, delayMs, supplier, matcher);
    }

    public WaitResult<Boolean> wait(Callable<Boolean> supplier) {
        return waiter.waitIgnoreExceptions(temeoutS, delayMs, supplier);
    }
}
