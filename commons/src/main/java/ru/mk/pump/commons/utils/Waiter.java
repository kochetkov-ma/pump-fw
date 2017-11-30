package ru.mk.pump.commons.utils;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsAnything;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "WeakerAccess"})
public class Waiter implements Cloneable {

    private TimeUnit timeoutUnit = TimeUnit.SECONDS;

    /**
     * ALL ASSERTS. CAN BE CLEAN
     */
    private Class<? extends Throwable>[] DEFAULT_NOT_IGNORED_THROWABLE = new Class[]{Error.class};

    private Set<Class<? extends Throwable>> notIgnoringException = Sets.newHashSet();

    private Set<Class<? extends Throwable>> ignoringException = Sets.newHashSet();

    private boolean reThrow = true;

    /**
     * default enable ignoring AssertionError.class
     */
    public Waiter() {
        this(true);
    }

    /**
     * @param notIgnoringAsserts disable/enable ignoring AssertionError.class
     */
    public Waiter(boolean notIgnoringAsserts) {
        if (!notIgnoringAsserts) {
            DEFAULT_NOT_IGNORED_THROWABLE = new Class[0];
        } else {
            notIgnoringException.addAll(Arrays.asList(DEFAULT_NOT_IGNORED_THROWABLE));
        }
    }

    public static void sleep(int timeoutMS) {
        try {
            Thread.sleep(timeoutMS);
        } catch (InterruptedException ignore) {
        }
    }

    /**
     * Full clear notIgnoringException and ignoringException
     */
    public Waiter clear() {
        notIgnoringException.clear();
        ignoringException.clear();
        return this;
    }

    /**
     * Priority is greater than {@link #withIgnoreExceptions(Class)})
     */
    public Waiter withNotIgnoreExceptions(@NotNull Class<? extends Throwable> notIgnoringThrowable) {
        notIgnoringException.add(notIgnoringThrowable);
        return this;
    }

    public Waiter withTimeoutUnit(TimeUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
        return this;
    }

    public Waiter withReThrow(boolean reThrow) {
        this.reThrow = reThrow;
        return this;
    }

    /**
     * Priority is less than {@link #withNotIgnoreExceptions(Class)})
     */
    public Waiter withIgnoreExceptions(@NotNull Class<? extends Throwable> ignoringThrowable) {
        ignoringException.add(ignoringThrowable);
        return this;
    }

    public WaitResult wait(int timeout, int intervalMs, @NotNull Callable<Boolean> successCondition) {
        return wait(timeout, intervalMs, successCondition, Matchers.is(true));
    }

    public WaitResult<Boolean> waitIgnoreExceptions(int timeout, int intervalMs, @NotNull Callable<Boolean> successCondition) {
        final Waiter clone = this.clone();
        clone.notIgnoringException.addAll(Arrays.asList(DEFAULT_NOT_IGNORED_THROWABLE));
        clone.ignoringException.clear();
        clone.withIgnoreExceptions(Throwable.class);
        return clone.wait(timeout, intervalMs, successCondition);
    }

    public <T> WaitResult<T> wait(int timeout, int intervalMs, @NotNull Callable<T> action, @Nullable Matcher<T> matcher) {

        if (matcher == null) {
            matcher = new IsAnything();
        }
        T result;
        final AtomicLong elapsedTime = new AtomicLong(timeout * 1000);
        try {
            result = Awaitility.await()
                .conditionEvaluationListener(condition -> elapsedTime.set(condition.getElapsedTimeInMS()))
                .ignoreExceptionsMatching(this::isIgnore)
                .pollInterval(intervalMs, TimeUnit.MILLISECONDS)
                .atMost(timeout, timeoutUnit)
                .until(action, matcher);
        } catch (Throwable ex) {
            if (reThrow && !isIgnore(ex) && !(ex instanceof ConditionTimeoutException)) {
                throw ex;
            }
            try {
                result = action.call();
            } catch (Throwable throwable) {
                return this.<T>resultDelegate(false, elapsedTime.get()).withInfo(timeout, intervalMs).withCause(throwable);
            }
            return this.<T>resultDelegate(false, elapsedTime.get()).withInfo(timeout, intervalMs).withResult(result);
        }

        if (result != null) {
            return this.<T>resultDelegate(true, elapsedTime.get()).withResult(result).withInfo(timeout, intervalMs);
        } else {
            return this.<T>resultDelegate(true, elapsedTime.get()).withInfo(timeout, intervalMs);
        }
    }

    public <T> WaitResult<T> waitIgnoreExceptions(int timeout, int intervalMs, @NotNull Callable<T> action, @Nullable Matcher<T> matcher) {
        final Waiter clone = this.clone();
        clone.notIgnoringException.addAll(Arrays.asList(DEFAULT_NOT_IGNORED_THROWABLE));
        clone.ignoringException.clear();
        clone.withIgnoreExceptions(Throwable.class);
        return clone.wait(timeout, intervalMs, action, matcher);
    }

    protected <T> WaitResult<T> resultDelegate(boolean success, long elapsedTime) {
        return new WaitResult<>(success, elapsedTime);
    }

    @Override
    public Waiter clone() {
        Waiter waiter = null;
        try {
            waiter = (Waiter) super.clone();
            waiter.ignoringException = new HashSet<>(ignoringException);
            waiter.notIgnoringException = new HashSet<>(notIgnoringException);
            waiter.DEFAULT_NOT_IGNORED_THROWABLE = DEFAULT_NOT_IGNORED_THROWABLE.clone();
        } catch (CloneNotSupportedException ex) {
            log.error("Error while cloning waiter", ex);
        }
        return waiter;
    }

    private boolean isIgnore(Throwable throwable) {
        //noinspection SimplifiableIfStatement
        if (notIgnoringException.stream().anyMatch(throwableClass -> throwableClass.isAssignableFrom(throwable.getClass()))) {
            return false;
        }
        return ignoringException.stream().anyMatch(throwableClass -> throwableClass.isAssignableFrom(throwable.getClass()));

    }
}
