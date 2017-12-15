package ru.mk.pump.web.browsers;

import java.util.Observer;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import ru.mk.pump.commons.activity.AbstractActivity;
import ru.mk.pump.commons.activity.Activity;

@Slf4j
@ToString(callSuper = true)
public final class Window extends AbstractActivity {

    private final WebDriver driver;

    private Window(Observer observer, String uuid, WebDriver driver) {
        super(observer, uuid);
        this.driver = driver;
    }

    public static Window of(Observer observer, WebDriver driver, String windowDriverUuid) {
        return new Window(observer, windowDriverUuid, driver);
    }

    @Override
    public Activity activate() {
        if (getUUID().equals(driver.getWindowHandle())) {
            driver.switchTo().window(getUUID());
        }
        return super.activate();
    }

    @Override
    public void close() {
        driver.switchTo().window(getUUID());
        driver.close();
        super.close();

    }

}
