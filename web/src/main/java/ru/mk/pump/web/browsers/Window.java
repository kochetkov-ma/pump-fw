package ru.mk.pump.web.browsers;

import static ru.mk.pump.commons.constants.Patterns.UUID_PATTERN;

import java.util.Observer;
import java.util.UUID;
import java.util.regex.Matcher;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import ru.mk.pump.commons.activity.AbstractActivity;
import ru.mk.pump.commons.activity.Activity;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.exceptions.BrowserException;

@Slf4j
@ToString(callSuper = true, of = {"type"})
public final class Window extends AbstractActivity {

    private final WebDriver driver;

    @Getter
    private final String type;

    private Window(Observer observer, UUID uuid, String type, WebDriver driver) {
        super(observer, uuid);
        this.type = type;
        this.driver = driver;
    }

    public static Window of(Observer observer, WebDriver driver, String windowDriverUuid) {
        final Matcher matcher = UUID_PATTERN.matcher(windowDriverUuid);
        if (matcher.find() && matcher.groupCount() > 1) {
            try {
                return new Window(observer, UUID.fromString(matcher.group(2)), matcher.group(1), driver);
            } catch (Exception ex) {
                throw new BrowserException(String.format("Error parsing uuid '%s' WebDriver Window", matcher.group(2)), ex);
            }
        } else {
            throw new BrowserException(String.format("Error parsing uuid '%s' WebDriver Window", windowDriverUuid));
        }
    }

    @Override
    public Activity activate() {
        if (!getFullId().equals(driver.getWindowHandle())) {
            driver.switchTo().window(getFullId());
        }
        return super.activate();
    }

    @Override
    public void close() {
        driver.switchTo().window(getFullId());
        driver.close();
        super.close();

    }

    private String getFullId() {
        if (Strings.isEmpty(getType())) {
            return getUUID().toString();
        } else {
            return getType() + "-" + getUUID().toString();
        }
    }
}
