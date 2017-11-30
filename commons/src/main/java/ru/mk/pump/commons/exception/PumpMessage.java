package ru.mk.pump.commons.exception;

import static ru.mk.pump.commons.constants.StringConstants.KEY_VALUE_PRETTY_DELIMITER;
import static ru.mk.pump.commons.constants.StringConstants.LINE;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mk.pump.commons.constants.StringConstants;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;
import ru.mk.pump.commons.utils.Strings;

@SuppressWarnings({"WeakerAccess", "unused"})
public class PumpMessage implements PrettyPrinter {

    private static final int OFFSET = 3;

    private static final String BLOCK_DELIMITER = "---";

    /**
     * Required
     */
    @Getter
    private final String title;

    /**
     * Optional
     */
    @Getter
    private String description;

    /**
     * Optional
     */
    @Getter
    private List<String> envInformation = Lists.newArrayList();

    /**
     * Optional
     */
    @Getter
    private List<String> extraInformation = Lists.newArrayList();

    /**
     * default is null for does not add by StringJoiner
     */
    @Getter
    private String preTitleMessage = null;

    public PumpMessage(String title, String description, Map<String, String> extraInformation, Map<String, String> envInformation) {
        this.title = title;
        if (description != null) {
            this.description = description;
        } else {
            this.description = "";
        }
        if (Objects.nonNull(envInformation)) {
            addInfo(this.envInformation, envInformation);
        }
        if (Objects.nonNull(extraInformation)) {
            addInfo(this.extraInformation, extraInformation);
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
            sb.append("[Description]")
                .append(Strings.toPrettyString(Arrays.asList(description.split(LINE)), OFFSET))
                .append(StringConstants.LINE);
        }
        if (!extraInformation.isEmpty()) {
            sb.append("[Additional information]").append(StringConstants.LINE).append(Strings.toPrettyString(extraInformation, OFFSET)).append(LINE);
        }
        if (!envInformation.isEmpty()) {
            sb.append("[Environment information]").append(StringConstants.LINE).append(Strings.toPrettyString(envInformation, OFFSET)).append(LINE);
        }
        return Strings.trimEnd(sb.toString());
    }

    public PumpMessage withDesc(String description) {
        if (description != null) {
            this.description = description;
        } else {
            this.description = "";
        }
        return this;
    }

    public PumpMessage addEnvInfo(@Nullable String name, @Nullable StrictInfo strictInfo) {
        if (strictInfo != null) {
            envInformation.add(BLOCK_DELIMITER + name + BLOCK_DELIMITER);
            addInfo(envInformation, strictInfo.getInfo());
        }
        return this;
    }

    public PumpMessage addExtraInfo(@Nullable String name, @Nullable StrictInfo strictInfo) {
        if (strictInfo != null) {
            extraInformation.add(BLOCK_DELIMITER + name + BLOCK_DELIMITER);
            addInfo(extraInformation, strictInfo.getInfo());
        }
        return this;
    }

    public PumpMessage addEnvInfo(@Nullable StrictInfo strictInfo) {
        return addEnvInfo("", strictInfo);
    }

    public PumpMessage addEnvInfo(@Nullable Map<String, String> envInfo) {
        if (envInfo != null) {
            addInfo(envInformation, envInfo);
        }
        return this;
    }

    public PumpMessage addExtraInfo(@Nullable StrictInfo strictInfo) {
        return addExtraInfo("", strictInfo);
    }

    public PumpMessage addExtraInfo(@Nullable Map<String, String> extraInfo) {
        if (extraInfo != null) {
            addInfo(extraInformation, extraInfo);
        }
        return this;
    }

    public PumpMessage addEnvInfo(@NotNull String envInformationKey, @Nullable String envInformationValue) {
        this.envInformation.addAll(line(envInformationKey, envInformationValue));
        return this;
    }

    public PumpMessage addExtraInfo(@NotNull String extraInformationKey, @Nullable String extraInformationValue) {
        this.extraInformation.addAll(line(extraInformationKey, extraInformationValue));
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

    //region PRIVATE
    private void addInfo(List<String> source, Map<String, String> strictInfoMap) {
        strictInfoMap.forEach((key, value) -> source.addAll(line(key, value)));
    }

    private List<String> line(String key, String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        final List<String> valueLines = Arrays.asList(value.split(LINE));
        if (valueLines.size() > 1) {
            final ImmutableList.Builder<String> builder = ImmutableList.<String>builder()
                .add(key + KEY_VALUE_PRETTY_DELIMITER + valueLines.get(0));
            valueLines.subList(1, valueLines.size()).forEach(i -> builder.add(keySize(key) + i));
            return builder.build();
        } else {
            return ImmutableList.of(key + KEY_VALUE_PRETTY_DELIMITER + value);
        }
    }

    private String keySize(String key) {
        return Strings.space(key.length() + KEY_VALUE_PRETTY_DELIMITER.length());
    }
    //endregion
}
