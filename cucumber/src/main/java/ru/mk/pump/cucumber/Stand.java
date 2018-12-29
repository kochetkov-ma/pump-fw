package ru.mk.pump.cucumber;

import ru.mk.pump.commons.interfaces.PrettyPrinter;

import java.nio.file.Path;

public interface Stand extends PrettyPrinter {
    Path getPropertiesFile();
}