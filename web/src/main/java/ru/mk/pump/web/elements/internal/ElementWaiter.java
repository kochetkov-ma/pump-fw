package ru.mk.pump.web.elements.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.web.configuration.ConfigurationHolder;

@Slf4j
@SuppressWarnings({"WeakerAccess", "unused"})
public class ElementWaiter {

    public static int DEFAULT_TIMEOUT_S = ConfigurationHolder.get().getElement().getStateTimeout();

    @Getter
    private final int timeout;

    private Waiter waiter;

    private int delayMs;

    //region CONSTRUCTOR
    private ElementWaiter(int timeout, int delayMs, Waiter waiter) {
        this.timeout = timeout;
        this.delayMs = 50;
        this.waiter = waiter;
    }

    private ElementWaiter(int timeout, int delayMs, TimeUnit timeoutUnit) {
        this(timeout, delayMs, new Waiter().withTimeoutUnit(timeoutUnit));
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

    public ElementWaiter setReThrow(boolean reThrow) {
        this.waiter.withReThrow(reThrow);
        return this;
    }

    public ElementWaiter withTimeoutMs(int timeoutMs) {
        return new ElementWaiter(timeoutMs, delayMs, TimeUnit.MILLISECONDS);
    }

    public ElementWaiter withTimeoutS(int timeoutS) {
        return new ElementWaiter(timeoutS, delayMs, TimeUnit.SECONDS);
    }

    public ElementWaiter withTimeoutUnit(TimeUnit timeoutUnit) {
        return new ElementWaiter(timeout, delayMs, timeoutUnit);
    }

    public ElementWaiter withDelay(int delayMs) {
        return new ElementWaiter(timeout, delayMs, waiter);
    }

    public ElementWaiter setNotIgnoreExceptions(@NonNull Class<? extends Throwable> notIgnoringThrowable) {
        waiter.withNotIgnoreExceptions(notIgnoringThrowable);
        return this;
    }

    public static <T> VariabilityMatcher<T> newVariabilityMatcher(BiPredicate<T, T> prevMatchPredicate) {
        return new VariabilityMatcher<>(prevMatchPredicate);
    }

    static class VariabilityMatcher<T> extends BaseMatcher<T> {

        private Object prevItem;

        private final BiPredicate<T, T> prevMatchPredicate;

        VariabilityMatcher(BiPredicate<T, T> prevMatchPredicate) {
            this.prevMatchPredicate = prevMatchPredicate;
        }

        @Override
        public boolean matches(Object item) {
            boolean res;
            //noinspection unchecked
            res = prevItem != null && prevMatchPredicate.test((T) prevItem, (T) item);
            prevItem = item;
            return res;
        }

        @Override
        public void describeTo(Description description) {

        }
    }

    public <T> WaitResult<T> waitPredicate(Callable<T> supplier, BiPredicate<T, T> prevMatchPredicate) {
        return waiter.waitIgnoreExceptions(timeout, delayMs, supplier, newVariabilityMatcher(prevMatchPredicate));
    }

    public <T> WaitResult<T> wait(Callable<T> supplier, Matcher<T> matcher) {
        return waiter.waitIgnoreExceptions(timeout, delayMs, supplier, matcher);
    }

    public WaitResult<Boolean> wait(Callable<Boolean> supplier) {
        return waiter.waitIgnoreExceptions(timeout, delayMs, supplier);
    }
}
