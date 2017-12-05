package ru.mk.pump.commons.constants;

import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Patterns {

    public static final Pattern UUID_PATTERN = Pattern.compile("(.*)-?(.{8}-.{4}-.{4}-.{4}-.{12})");
}
