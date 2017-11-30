package ru.mk.pump.web.elements.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.commons.utils.Waiter;

@Slf4j
@SuppressWarnings({"WeakerAccess", "unused"})
public class ElementWaiter{

    public static final int DEFAULT_TIMEOUT_S = 1;

    @Getter
    private final int timeoutS;

    private Waiter waiter;

    private int delayMs;

    //region CONSTRUCTOR
    private ElementWaiter(int timeout, int delayMs, TimeUnit timeoutUnit) {
        this.timeoutS = timeout;
        this.delayMs = 50;
        this.waiter = new Waiter().withTimeoutUnit(timeoutUnit);
    }

    private ElementWaiter(int timeout, int delayMs) {
        this(timeout, delayMs, TimeUnit.SECONDS);
    }

    private ElementWaiter() {
        this(DEFAULT_TIMEOUT_S, 0);
    }
    //endregion

    //region NEW
    public static ElementWaiter newWaiterMs(int timeoutMs) {
        return new ElementWaiter(timeoutMs, 0, TimeUnit.MILLISECONDS);
    }

    public static ElementWaiter newWaiterS(int timeoutMs) {
        return new ElementWaiter(timeoutMs, 0);
    }

    public static ElementWaiter newWaiterS() {
        return new ElementWaiter();
    }
    //endregion

    public ElementWaiter clear() {
        waiter.clear();
        return this;
    }

    public ElementWaiter withReThrow(boolean reThrow) {
        this.waiter.withReThrow(reThrow);
        return this;
    }

    public ElementWaiter withDelay(int delayMs) {
        this.delayMs = delayMs;
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
}
