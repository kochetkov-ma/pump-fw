package ru.mk.pump.web.elements.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.commons.utils.Waiter.WaitResult;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ElementWaiter {

    public static final int DEFAULT_TIMEOUT_S = 10;

    @Getter
    private final int timeoutS;

    private final Waiter waiter;

    private int delayMs;

    private ElementWaiter(int timeoutMs) {

        this.timeoutS = timeoutMs;
        this.delayMs = 50;
        this.waiter = new Waiter();
        try {
            FieldUtils.writeField(waiter, "TIMEOUT_UNITS", TimeUnit.MILLISECONDS, true);
        } catch (IllegalAccessException ignore) {
        }
    }

    public ElementWaiter(int timeoutS, int delayMs) {

        this.timeoutS = timeoutS;
        this.delayMs = delayMs;
        this.waiter = new Waiter();
        //waiter.withNotIgnoreExceptions(BrowserException.class);
    }

    public ElementWaiter() {
        this(DEFAULT_TIMEOUT_S, 0);
    }

    static ElementWaiter newFastInstance(@SuppressWarnings("SameParameterValue") int timeoutMs) {
        return new ElementWaiter(timeoutMs);
    }

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

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public ElementWaiter clone() {
        return new ElementWaiter(this.timeoutS, this.delayMs);
    }
}
