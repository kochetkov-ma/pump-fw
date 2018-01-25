package ru.mk.pump.commons.utils;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.exception.TimeoutException;
import ru.mk.pump.commons.interfaces.StrictInfo;

//region INNER STATIC CLASS
@SuppressWarnings({"UnusedReturnValue", "WeakerAccess", "unused"})
public class WaitResult<T> implements StrictInfo {

    @Getter
    private final boolean success;

    @Getter
    private final long elapsedTime;

    @Getter
    private Throwable cause;

    @Getter
    private T result;

    private int timeout = -1;

    private int intervalMs = -1;

    private Function<WaitResult<T>, ? extends RuntimeException> exceptionOnFail = (w) ->
            new TimeoutException(new PumpMessage("Waiter timeout exception", null, w.getInfo()), w.getCause());

    public static WaitResult<Boolean> trueResult(long elapsedTime, int timeout) {
        return new WaitResult<Boolean>(true, elapsedTime).withInfo(timeout, -1).withResult(false);
    }

    public static WaitResult<Boolean> falseResult(long elapsedTime, int timeout, Throwable cause) {
        return new WaitResult<Boolean>(false, elapsedTime).withInfo(timeout, -1).withResult(false).withCause(cause);
    }

    public static WaitResult<Boolean> empty() {
        return new WaitResult<>(false, 0);
    }

    public WaitResult(boolean success, long elapsedTime) {

        this.success = success;
        this.elapsedTime = elapsedTime;
    }

    public WaitResult<T> withResult(T result) {

        this.result = result;
        return this;
    }

    public WaitResult<T> withInfo(int timeout, int intervalMs) {
        this.timeout = timeout;
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
            throw newExceptionWithWaiterInfo.apply(this);
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
        final LinkedHashMap<String, String> stringMap = Maps.newLinkedHashMap();

        if (timeout != -1) {
            stringMap.put("timeout (sec)", String.valueOf(timeout));
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
        if (timeout != -1) {
            sb.append(", timeout=").append(timeout);
        }
        if (intervalMs != -1) {
            sb.append(", intervalMs=").append(intervalMs);
        }
        sb.append(')');
        return sb.toString();
    }

}
