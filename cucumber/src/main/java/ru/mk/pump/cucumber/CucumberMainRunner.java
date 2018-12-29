package ru.mk.pump.cucumber;

import cucumber.api.cli.Main;
import org.apache.commons.lang3.ArrayUtils;

public class CucumberMainRunner {

    public static String[] ARGS_TO_MERGE = {"--plugin", "ru.mk.pump.cucumber.plugin.PumpCucumberPlugin"};

    public static void main(String[] argv) {
        Main.main(ArrayUtils.addAll(argv, ARGS_TO_MERGE));
    }
}
