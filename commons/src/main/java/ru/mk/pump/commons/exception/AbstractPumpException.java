package ru.mk.pump.commons.exception;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.interfaces.StrictInfo;

@SuppressWarnings({"WeakerAccess", "unused"})
@Slf4j
abstract class AbstractPumpException extends RuntimeException {

    @Getter(AccessLevel.PROTECTED)
    private final Map<String, StrictInfo> env = Maps.newHashMap();

    @Getter(AccessLevel.PROTECTED)
    private final Map<String, StrictInfo> target = Maps.newHashMap();

    @Getter
    private final PumpMessage sourceMessage;


    private volatile boolean freeze = false;

    public AbstractPumpException(PumpMessage exceptionMessage) {
        this(exceptionMessage, null);

    }

    public AbstractPumpException(PumpMessage exceptionMessage, @Nullable Throwable cause) {
        super(cause);
        this.sourceMessage = exceptionMessage;
    }

    @Override
    public String getMessage() {
        initInfo();
        return sourceMessage.toPrettyString();
    }

    protected AbstractPumpException addEnv(String name, StrictInfo info) {
        if (canAddEnv(name, getCause())) {
            env.put(name, info);
        }
        return this;
    }

    protected AbstractPumpException addTarget(String name, StrictInfo info) {

        if (canAddTarget(name, getCause())) {
            target.put(name, info);
        }
        return this;
    }

    private boolean canAddTarget(String nameToCheck, Throwable cause) {
        if (cause != null && cause != this && cause instanceof AbstractPumpException) {
            final AbstractPumpException exception = (AbstractPumpException) cause;
            if (exception.getTarget() != null && !exception.getTarget().containsKey(nameToCheck)) {
                return canAddTarget(nameToCheck, cause.getCause());
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean canAddEnv(String nameToCheck, Throwable cause) {
        if (cause != null &&cause != this && cause instanceof AbstractPumpException) {
            final AbstractPumpException exception = (AbstractPumpException) cause;
            if (exception.getEnv() != null && !exception.getEnv().containsKey(nameToCheck)) {
                return canAddEnv(nameToCheck, cause.getCause());
            } else {
                return false;
            }
        }
        return true;
    }

    private synchronized void initInfo() {
        if (!freeze) {
            getEnv().forEach((key, value) -> getSourceMessage().addEnvInfo(key, value));
            getTarget().forEach((key, value) -> getSourceMessage().addExtraInfo(key, value));
            freeze = true;
        }
    }
}
