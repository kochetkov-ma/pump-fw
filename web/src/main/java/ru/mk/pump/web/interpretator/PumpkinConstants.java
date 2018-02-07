package ru.mk.pump.web.interpretator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PumpkinConstants {

    public static final String TITLE_PATTERN = "^[a-zA-ZА-Яа-я].*";
    public static final String GROOVY_PATTERN = "^\\$groovy\\{(.+)}.*";
    public static final String TEST_VAR_PATTERN = "^\\$\\{(.+)}.*";

}
