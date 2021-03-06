package ru.mk.pump.web.browsers.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openqa.selenium.Dimension;
import ru.mk.pump.commons.config.Property;
import ru.mk.pump.commons.utils.Str;
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
@NoArgsConstructor
public final class Size {

    @Getter
    @Property(value = "device", required = false)
    private String deviceName;

    @Getter
    private boolean defaultSize;

    @Getter
    @Property(value = "fullscreen", required = false)
    private boolean fullScreen;

    @Getter
    @Property(value = "x", required = false)
    private int x;

    @Getter
    @Property(value = "y", required = false)
    private int y;

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
        return !Str.isEmpty(deviceName);
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
