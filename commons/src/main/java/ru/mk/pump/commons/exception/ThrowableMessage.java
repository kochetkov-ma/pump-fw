package ru.mk.pump.commons.exception;

import static ru.mk.pump.commons.constants.StringConstants.LINE;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.NonNull;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.utils.Strings;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ThrowableMessage implements PrettyPrinter {

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

    public ThrowableMessage(String title, String description, Map<String, String> extraInformation, Map<String, String> envInformation) {
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

    public ThrowableMessage(@NonNull String title, String description, Map<String, String> extraInformation) {
        this(title, description, extraInformation, null);
    }

    public ThrowableMessage(@NonNull String title, String description) {
        this(title, description, null, null);
    }

    public ThrowableMessage(@NonNull String title) {
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
            sb.append("[Additional information]").append(StringConstants.LINE).append(Strings.mapToPrettyString(extraInformation));
        }
        if (!envInformation.isEmpty()) {
            sb.append("[Environment information]").append(StringConstants.LINE).append(Strings.mapToPrettyString(envInformation)).append(LINE);
        }
        return Strings.trim(sb.toString());
    }

    public ThrowableMessage withDesc(String description) {
        this.description = description;
        return this;
    }

    public ThrowableMessage addEnvInfo(String envInformationKey, String envInformationValue) {
        this.envInformation.put(envInformationKey, envInformationValue);
        return this;
    }

    public ThrowableMessage addExtraInfo(String extraInformationKey, String extraInformationValue) {
        this.extraInformation.put(extraInformationKey, extraInformationValue);
        return this;
    }

    public ThrowableMessage withPre(String preTitleMessage) {
        this.preTitleMessage = preTitleMessage;
        return this;
    }

    @Override
    public String toString() {
        return "ThrowableMessage{" +
            "title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", envInformation=" + envInformation +
            ", extraInformation=" + extraInformation +
            '}';
    }
}
