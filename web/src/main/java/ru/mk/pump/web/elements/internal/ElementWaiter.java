package ru.mk.pump.web.elements.internal;

import lombok.Getter;
import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.commons.utils.Waiter.WaitResult;

import java.util.concurrent.Callable;

public class ElementWaiter {

    public static final int DEFAULT_TIMEOUT_S = 10;

    @Getter
    private final int timeoutS;

    private final int delayMs;

    private final Waiter waiter;

    public ElementWaiter(int timeoutS, int delayMs) {

        this.timeoutS = timeoutS;
        this.delayMs = delayMs;
        this.waiter = new Waiter();
    }

    public ElementWaiter() {
        this(DEFAULT_TIMEOUT_S, 0);
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
        return waiter.waitIgnoreExceptions(timeoutS, delayMs, supplier, matcher);
    }

    public WaitResult<Boolean> wait(Callable<Boolean> supplier) {
        return waiter.waitIgnoreExceptions(timeoutS, delayMs, supplier);
    }

    public ElementWaiter newInstance() {
        return new ElementWaiter(this.timeoutS, this.delayMs);
    }
}
