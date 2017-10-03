package ru.mk.pump.commons.exception;

import static ru.mk.pump.commons.constants.StringConstants.KEY_VALUE_PRETTY_DELIMETER;
import static ru.mk.pump.commons.constants.StringConstants.LINE;

import com.google.common.collect.Maps;
import com.sun.istack.internal.NotNull;
import java.util.Map;
import ru.mk.pump.commons.interfaces.PrettyPrinting;
import ru.mk.pump.commons.utils.StringUtils;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ExceptionMessage implements PrettyPrinting {

    /**
     * Required
     */
    private final String title;

    /**
     * Optional
     */
    private final String description;

    /**
     * Optional
     */
    private final Map<String, String> envInformation;

    /**
     * Optional
     */
    private final Map<String, String> extraInformation;

    /**
     * default is null for does not add by StringJoiner
     */
    private String preTitleMessage = null;

    public ExceptionMessage(@NotNull String title, String description, Map<String, String> envInformation, Map<String, String> extraInformation) {
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

    public ExceptionMessage(@NotNull String title, String description, Map<String, String> envInformation) {
        this(title, description, envInformation, null);
    }

    public ExceptionMessage(@NotNull String title, String description) {
        this(title, description, null, null);
    }

    public ExceptionMessage(@NotNull String title) {
        this(title, null, null, null);
    }

    @Override
    public String toPrettyString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Title").append(KEY_VALUE_PRETTY_DELIMETER).append(StringUtils.oneLineConcat(preTitleMessage, title)).append(LINE);
        if (!description.isEmpty()) {
            sb.append("Description").append(KEY_VALUE_PRETTY_DELIMETER).append(description).append(LINE);
        }
        if (!envInformation.isEmpty()) {
            sb.append("Environment information").append(KEY_VALUE_PRETTY_DELIMETER).append(StringUtils.mapToPrettyString(envInformation)).append(LINE);
        }
        if (!extraInformation.isEmpty()) {
            sb.append("Additional information").append(KEY_VALUE_PRETTY_DELIMETER).append(StringUtils.mapToPrettyString(extraInformation));
        }
        return sb.toString();
    }

    public ExceptionMessage withPre(String preTitleMessage) {
        this.preTitleMessage = preTitleMessage;
        return this;
    }

    @Override
    public String toString() {
        return "ExceptionMessage{" +
            "title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", envInformation=" + envInformation +
            ", extraInformation=" + extraInformation +
            '}';
    }
}
