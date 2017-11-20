package ru.mk.pump.commons.exception;

import com.google.common.collect.Maps;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Strings;

import java.util.Map;

import static ru.mk.pump.commons.constants.StringConstants.LINE;

@SuppressWarnings({"WeakerAccess", "unused"})
public class PumpMessage implements PrettyPrinter {

    /**
     * Required
     */
    private final String title;

    /**
     * Optional
     */
    private String description;

    /**
     * Optional
     */
    private Map<String, String> envInformation;

    /**
     * Optional
     */
    private Map<String, String> extraInformation;

    /**
     * default is null for does not add by StringJoiner
     */
    private String preTitleMessage = null;

    public PumpMessage(String title, String description, Map<String, String> extraInformation, Map<String, String> envInformation) {
        this.title = title;
        if (description != null) {
            this.description = description;
        } else {
            this.description = "";
        }
        if (envInformation != null) {
            this.envInformation = envInformation;
        } else {
            this.envInformation = Maps.newHashMap();
        }
        if (extraInformation != null) {
            this.extraInformation = extraInformation;
        } else {
            this.extraInformation = Maps.newHashMap();
        }
    }

    public PumpMessage(@NonNull String title, String description, Map<String, String> extraInformation) {
        this(title, description, extraInformation, null);
    }

    public PumpMessage(@NonNull String title, String description) {
        this(title, description, null, null);
    }

    public PumpMessage(@NonNull String title) {
        this(title, null, null, null);
    }

    @Override
    public String toPrettyString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(Strings.concat(StringConstants.DOT_SPACE, preTitleMessage, title)).append(LINE);
        if (!description.isEmpty()) {
            sb.append("[Description]").append(StringConstants.KEY_VALUE_PRETTY_DELIMITER).append(description).append(LINE);
        }
        if (!extraInformation.isEmpty()) {
            sb.append("[Additional information]").append(StringConstants.LINE).append(Strings.mapToPrettyString(extraInformation)).append(LINE);
        }
        if (!envInformation.isEmpty()) {
            sb.append("[Environment information]").append(StringConstants.LINE).append(Strings.mapToPrettyString(envInformation)).append(LINE);
        }
        return Strings.trim(sb.toString());
    }

    public PumpMessage withDesc(String description) {
        if (description != null) {
            this.description = description;
        } else {
            this.description = "";
        }
        return this;
    }

    public PumpMessage addEnvInfo(@NotNull StrictInfo strictInfo) {
        this.envInformation.putAll(strictInfo.getInfo());
        return this;
    }

    public PumpMessage addEnvInfo(@NotNull Map<String, String> envInfo) {
        this.envInformation.putAll(envInfo);
        return this;
    }

    public PumpMessage addExtraInfo(@NotNull StrictInfo strictInfo) {
        this.extraInformation.putAll(strictInfo.getInfo());
        return this;
    }

    public PumpMessage addExtraInfo(@NotNull Map<String, String> extraInfo) {
        this.extraInformation.putAll(extraInfo);
        return this;
    }

    public PumpMessage addEnvInfo(@NotNull String envInformationKey, @Nullable String envInformationValue) {
        this.envInformation.put(envInformationKey, envInformationValue);
        return this;
    }

    public PumpMessage addExtraInfo(@NotNull String extraInformationKey, @Nullable String extraInformationValue) {
        this.extraInformation.put(extraInformationKey, extraInformationValue);
        return this;
    }

    public PumpMessage withPre(@Nullable String preTitleMessage) {
        this.preTitleMessage = preTitleMessage;
        return this;
    }

    @Override
    public String toString() {
        return "PumpMessage{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", envInformation=" + envInformation +
                ", extraInformation=" + extraInformation +
                '}';
    }
}