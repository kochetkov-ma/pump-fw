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
        env.put(name, info);
        return this;
    }

    protected AbstractPumpException addTarget(String name, StrictInfo info) {
        target.put(name, info);
        return this;
    }

    abstract protected Throwable checkCauseAndReorganize(Throwable cause);

    private synchronized void initInfo() {
        if (!freeze) {
            if (getCause() != null && getCause() != this) {
                checkCauseAndReorganize(getCause());
            }
            getEnv().forEach((key, value) -> getSourceMessage().addEnvInfo(key, value));
            getTarget().forEach((key, value) -> getSourceMessage().addExtraInfo(key, value));
            freeze = true;
        }
    }
}
