package ru.mk.pump.web.interpretator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PumpkinConstants {

    public static final String TITLE_PATTERN = "^[0-9a-zA-ZА-Яа-я_ ].*";

    public static final String GROOVY_PATTERN = "^\\$groovy\\{(.*)}.*";

    public static final String TEST_VAR_PATTERN = "^\\$\\{(.*)}.*";

    public static final String ESC_CHAR = "/";
}
