package ru.mk.pump.web.browsers.configuration;

import lombok.Getter;
import org.openqa.selenium.Dimension;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.constants.WebConstants;

/**
 * <ul>
 *  orders
 *  <li>if type deviceName then use device
 *  <li>if type fullScreen then use fullScreen
 *  <li>if type defaultSize then use default
 *  <li>if type X and Y then use there
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
        return new Size(x, y, false, null);
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

    public Dimension getDimension() {
        if (useSize()) {
            return new Dimension(x, y);
        } else {
            final String[] size = WebConstants.DEFAULT_FULLSCREEN.split(",");
            return new Dimension(Integer.valueOf(size[0]), Integer.valueOf(size[1]));
        }
    }
}
