package ru.mk.pump.commons.exception;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.interfaces.StrictInfo;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * {@link RuntimeException} is based on {@link PumpMessage} format.
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
@Slf4j
public class PumpException extends RuntimeException {

    @Getter(AccessLevel.PROTECTED)
    private final Map<String, StrictInfo> env;
    @Getter(AccessLevel.PROTECTED)
    private final Map<String, StrictInfo> extra;
    private String formattedMessage;
    private PumpMessage pumpMessage;

    //region Constructors
    public PumpException() {
        this((String) null);
    }

    public PumpException(@Nullable String title) {
        this(new PumpMessage(title));
    }

    public PumpException(@Nullable String title, @Nullable Throwable cause) {
        this(new PumpMessage(title), cause);
    }

    public PumpException(@NonNull PumpMessage pumpMessage) {
        this(pumpMessage, Maps.newHashMap(), Maps.newHashMap(), null);
    }

    public PumpException(@NonNull PumpMessage pumpMessage, @Nullable Throwable cause) {
        this(pumpMessage, Maps.newHashMap(), Maps.newHashMap(), cause);
    }

    public PumpException(@NonNull PumpMessage pumpMessage,
            @NonNull Map<String, StrictInfo> extra,
            @NonNull Map<String, StrictInfo> env,
            @Nullable Throwable cause) {

        super(cause);
        this.pumpMessage = pumpMessage;
        this.extra = extra;
        this.env = env;
    }

    //endregion
    public PumpException withTitle(@Nullable String title) {
        if (pumpMessage != null) {
            this.pumpMessage = new PumpMessage(title)
                    .withPre(this.pumpMessage.getPreTitleMessage())
                    .withDesc(this.pumpMessage.getDescription())
                    .setExtraInformation(this.pumpMessage.getExtraInformation())
                    .setEnvInformation(this.pumpMessage.getEnvInformation());
        } else {
            this.pumpMessage = new PumpMessage(title);
        }
        this.formattedMessage = generateMessage(pumpMessage, extra, env, getCause());
        return this;
    }

    public PumpException withCause(@Nullable Throwable cause) {
        initCause(cause);
        this.formattedMessage = generateMessage(pumpMessage, extra, env, getCause());
        return this;
    }

    public PumpException withExtra(@NonNull String name, @NonNull String value) {
        this.formattedMessage = generateMessage(pumpMessage.addExtraInfo(name, value), extra, env, getCause());
        return this;
    }

    public PumpException withExtra(@NonNull String name, @NonNull StrictInfo value) {
        extra.put(name, value);
        this.formattedMessage = generateMessage(pumpMessage, extra, env, getCause());
        return this;
    }

    public PumpException withEnv(@NonNull String name, @NonNull String value) {
        this.formattedMessage = generateMessage(pumpMessage.addEnvInfo(name, value), extra, env, getCause());
        return this;
    }

    public PumpException withEnv(@NonNull String name, @NonNull StrictInfo value) {
        env.put(name, value);
        this.formattedMessage = generateMessage(pumpMessage, extra, env, getCause());
        return this;
    }

    public PumpException withDesc(@NonNull String desc) {
        this.formattedMessage = generateMessage(pumpMessage.withDesc(desc), extra, env, getCause());
        return this;
    }

    public PumpException withPre(@NonNull String pre) {
        this.formattedMessage = generateMessage(pumpMessage.withPre(pre), extra, env, getCause());
        return this;
    }

    @Override
    public String getMessage() {
        return formattedMessage;
    }

    //region Private
    private static String generateMessage(@Nullable PumpMessage pumpMessage,
            @NonNull Map<String, StrictInfo> extra,
            @NonNull Map<String, StrictInfo> env,
            @Nullable Throwable cause) {

        final PumpMessage message;
        if (pumpMessage == null) {
            message = new PumpMessage("");
        } else {
            message = new PumpMessage(pumpMessage.getTitle())
                    .withPre(pumpMessage.getPreTitleMessage())
                    .withDesc(pumpMessage.getDescription())
                    .setEnvInformation(pumpMessage.getEnvInformation())
                    .setExtraInformation(pumpMessage.getExtraInformation());
        }
        extra.forEach((k, v) -> withExtra(message, k, v, cause));
        env.forEach((k, v) -> withEnv(message, k, v, cause));
        return message.toPrettyString();
    }

    private static PumpMessage withEnv(@NonNull PumpMessage pumpMessage,
            @NonNull String name,
            @NonNull StrictInfo info,
            @Nullable Throwable cause) {

        if (canAddEnv(name, cause)) {
            pumpMessage.addEnvInfo(name, info);
        }
        return pumpMessage;
    }

    private static PumpMessage withExtra(@NonNull PumpMessage pumpMessage,
            @NonNull String name,
            @NonNull StrictInfo info,
            @Nullable Throwable cause) {

        if (canAddEnv(name, cause)) {
            pumpMessage.addExtraInfo(name, info);
        }
        return pumpMessage;
    }

    private static boolean canAddTarget(String nameToCheck, Throwable cause) {

        if (cause instanceof PumpException) {
            final PumpException exception = (PumpException) cause;
            return exception.getExtra() != null
                    && !exception.getExtra().containsKey(nameToCheck)
                    && canAddTarget(nameToCheck, cause.getCause());
        }
        return true;
    }

    private static boolean canAddEnv(String nameToCheck, Throwable cause) {

        if (cause instanceof PumpException) {
            final PumpException exception = (PumpException) cause;
            return exception.getEnv() != null
                    && !exception.getEnv().containsKey(nameToCheck)
                    && canAddEnv(nameToCheck, cause.getCause());
        }
        return true;
    }
    //endregion
}