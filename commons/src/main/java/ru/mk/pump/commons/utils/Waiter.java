package ru.mk.pump.commons.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsAnything;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.exception.TimeoutException;
import ru.mk.pump.commons.interfaces.StrictInfo;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "WeakerAccess"})
public class Waiter {

    /**
     * ALL ASSERTS. CAN BE CLEAN
     */
    private Class<? extends Throwable>[] DEFAULT_NOT_IGNORED_THROWABLE = new Class[]{AssertionError.class};

    private Set<Class<? extends Throwable>> notIgnoringException = Sets.newHashSet();

    private Set<Class<? extends Throwable>> ignoringException = Sets.newHashSet();

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

    /**
     * Priority is less than {@link #withNotIgnoreExceptions(Class)})
     */
    public Waiter withIgnoreExceptions(@NotNull Class<? extends Throwable> ignoringThrowable) {
        ignoringException.add(ignoringThrowable);
        return this;
    }

    public WaitResult wait(int timeoutSec, int intervalMs, @NotNull Callable<Boolean> successCondition) {
        return wait(timeoutSec, intervalMs, successCondition, Matchers.is(true));
    }

    public WaitResult<Boolean> waitIgnoreExceptions(int timeoutSec, int intervalMs, @NotNull Callable<Boolean> successCondition) {
        notIgnoringException = Sets.newHashSet(DEFAULT_NOT_IGNORED_THROWABLE);
        ignoringException.clear();
        withIgnoreExceptions(Throwable.class);
        return wait(timeoutSec, intervalMs, successCondition);
    }

    public <T> WaitResult<T> wait(int timeoutSec, int intervalMs, @NotNull Callable<T> action, @Nullable Matcher<T> matcher) {

        if (matcher == null) {
            matcher = new IsAnything();
        }
        T result;
        final AtomicLong elapsedTime = new AtomicLong(timeoutSec * 1000);
        try {
            result = Awaitility.await()
                    .conditionEvaluationListener(condition -> elapsedTime.set(condition.getElapsedTimeInMS()))
                    .ignoreExceptionsMatching(this::checkException)
                    .pollInterval(intervalMs, TimeUnit.MILLISECONDS)
                    .atMost(timeoutSec, TimeUnit.SECONDS)
                    .until(action, matcher);
        } catch (Exception ex) {
            try {
                result = action.call();
            } catch (Throwable throwable) {
                return this.<T>resultDelegate(false, elapsedTime.get()).withInfo(timeoutSec, intervalMs).withCause(throwable);
            }
            return this.<T>resultDelegate(false, elapsedTime.get()).withInfo(timeoutSec, intervalMs).withResult(result);
        }

        if (result != null) {
            return this.<T>resultDelegate(true, elapsedTime.get()).withResult(result).withInfo(timeoutSec, intervalMs);
        } else {
            return this.<T>resultDelegate(true, elapsedTime.get()).withInfo(timeoutSec, intervalMs);
        }
    }

    public <T> WaitResult<T> waitIgnoreExceptions(int timeoutSec, int intervalMs, @NotNull Callable<T> action, @Nullable Matcher<T> matcher) {
        notIgnoringException = Sets.newHashSet(DEFAULT_NOT_IGNORED_THROWABLE);
        ignoringException.clear();
        withIgnoreExceptions(Throwable.class);
        return wait(timeoutSec, intervalMs, action, matcher);
    }

    protected <T> WaitResult<T> resultDelegate(boolean success, long elapsedTime) {
        return new WaitResult<>(success, elapsedTime);
    }

    //region INNER STATIC CLASS
    @SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
    public static class WaitResult<T> implements StrictInfo{

        @Getter
        private final boolean success;

        @Getter
        private final long elapsedTime;

        @Getter
        private Throwable cause;

        @Getter
        private T result;

        private int timeoutS = -1;

        private int intervalMs = -1;

        private Function<WaitResult<T>, ? extends RuntimeException> exceptionOnFail = (w) ->
                new TimeoutException(new PumpMessage("Waiter timeout exception", null, w.getInfo()), w.getCause());

        public static WaitResult<Boolean> trueResult(long elapsedTime, int timeoutS) {
            return new WaitResult(true, elapsedTime).withResult(false);
        }

        public static WaitResult<Boolean> falseResult(long elapsedTime, int timeoutS, Throwable cause) {
            return new WaitResult(false, elapsedTime).withResult(false).withCause(cause);
        }

        public static WaitResult<Boolean> empty() {
            return new WaitResult(false, 0);
        }

        public WaitResult(boolean success, long elapsedTime) {

            this.success = success;
            this.elapsedTime = elapsedTime;
        }

        public WaitResult<T> withResult(T result) {

            this.result = result;
            return this;
        }

        public WaitResult<T> withInfo(int timeoutS, int intervalMs) {
            this.timeoutS = timeoutS;
            this.intervalMs = intervalMs;
            return this;
        }

        public WaitResult<T> withCause(Throwable cause) {

            this.cause = cause;
            return this;
        }

        public boolean hasResult() {
            return result != null;
        }

        public boolean hasCause() {
            return cause != null;
        }

        public WaitResult<T> ifHasResult(@NotNull Consumer<T> consumer) {
            if (hasResult()) {
                consumer.accept(result);
            }
            return this;
        }

        public WaitResult<T> ifHasCause(@NotNull Consumer<Throwable> consumer) {
            if (hasCause()) {
                consumer.accept(cause);
            }
            return this;
        }

        public WaitResult<T> throwExceptionOnFail(@NotNull Function<WaitResult<T>, ? extends RuntimeException> newExceptionWithWaiterInfo) {
            if (!isSuccess()) {
                final RuntimeException ex = newExceptionWithWaiterInfo.apply(this);
                if (ex.getCause() == null && getCause() != null){
                    ex.initCause(getCause());
                }
                throw ex;
            }
            return this;
        }

        public WaitResult<T> withExceptionOnFail(@NotNull Function<WaitResult<T>, ? extends RuntimeException> newExceptionWithWaiterInfo) {
            this.exceptionOnFail = newExceptionWithWaiterInfo;
            return this;
        }

        public WaitResult<T> throwExceptionOnFail() {
            return throwExceptionOnFail(exceptionOnFail);
        }

        @Override
        public Map<String, String> getInfo() {
            final Map<String, String> stringMap = Maps.newLinkedHashMap();

            if (timeoutS != -1) {
                stringMap.put("timeout (sec)", String.valueOf(timeoutS));
            }
            if (intervalMs != -1) {
                stringMap.put("interval (ms)", String.valueOf(intervalMs));
            }
            stringMap.put("elapsed time (ms)", String.valueOf(elapsedTime));
            ifHasResult(r -> {
                String resString = r.toString();
                if (resString.length() > 50) {
                    resString = r.getClass().getSimpleName() + " too long string";
                }
                stringMap.put("last result", resString);
            });
            ifHasCause(t -> stringMap.put("cause", t.getClass().getSimpleName()));
            return stringMap;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("WaitResult(");
            sb.append("success=").append(success);
            sb.append(", elapsedTime=").append(elapsedTime);
            if (cause != null) {
                sb.append(", cause=").append(cause.getClass().getSimpleName());
            }
            if (result != null) {
                sb.append(", result=").append(result);
            }
            if (timeoutS != -1) {
                sb.append(", timeoutS=").append(timeoutS);
            }
            if (intervalMs != -1) {
                sb.append(", intervalMs=").append(intervalMs);
            }
            sb.append(')');
            return sb.toString();
        }

    }
    //endregion

    //region PRIVATE
    private boolean checkException(Throwable throwable) {
        //noinspection SimplifiableIfStatement
        if (notIgnoringException.stream().anyMatch(throwableClass -> throwableClass.isAssignableFrom(throwable.getClass()))) {
            return false;
        }
        return ignoringException.stream().anyMatch(throwableClass -> throwableClass.isAssignableFrom(throwable.getClass()));

    }
    //endregion

}
