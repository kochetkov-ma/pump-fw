package ru.mk.pump.web.browsers.configuration;

import lombok.Getter;
import ru.mk.pump.commons.utils.Strings;

/**
 * <ul>
 *  orders
 *  <li>if set deviceName then use device
 *  <li>if set fullScreen then use fullScreen
 *  <li>if set defaultSize then use default
 *  <li>if set X and Y then use there
 * </ul>
 */
public final class Size {

    @Getter
    private final String deviceName;

    @Getter
    private final boolean defaultSize;

    @Getter
    private final boolean fullScreen;

    @Getter
    private final int x;

    @Getter
    private final int y;

    private Size(int x, int y, boolean fullScreen, String deviceName) {
        this.x = x;
        this.y = y;
        this.fullScreen = fullScreen;
        this.deviceName = deviceName;
        this.defaultSize = !fullScreen && (x < 1 || y < 1);

    }

    public static Size of(int x, int y) {
        return new Size(0, 0, false, null);
    }

    public static Size of(boolean fullScreen) {
        return new Size(0, 0, fullScreen, null);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean useDevice() {
        return !Strings.isEmpty(deviceName);
    }

    public boolean useSize() {
        return !useDevice() && !fullScreen && !defaultSize;
    }
}
