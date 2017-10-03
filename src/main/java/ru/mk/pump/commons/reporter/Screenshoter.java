package ru.mk.pump.commons.reporter;

import java.util.Optional;

public interface Screenshoter {

    /**
     * @return can be null
     */
    Optional<byte[]> getScreen();
}
