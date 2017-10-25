package ru.mk.pump.commons.reporter;

import java.util.Optional;

public interface Screenshoter {

    Optional<byte[]> getScreen();
}
