package ru.mk.pump.web.browsers;

import java.util.Observer;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import ru.mk.pump.commons.activity.AbstractActivity;
import ru.mk.pump.commons.activity.Activity;
import ru.mk.pump.commons.activity.ActivityListener;
import ru.mk.pump.commons.activity.NamedEvent;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.utils.WebReporter;

@Slf4j
@ToString(callSuper = true)
public final class Window extends AbstractActivity {

    private final WebDriver driver;

    private Window(Observer observer, String uuid, WebDriver driver) {
        super(observer, uuid);
        this.driver = driver;
        addObserver(getDefaultListener());
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

    private Observer getDefaultListener() {
        return new ActivityListener() {
            @Override
            public void onClose(NamedEvent namedEvent, Activity activity) {
                report(namedEvent, activity);
            }

            @Override
            public void onActivate(NamedEvent namedEvent, Activity activity) {
                report(namedEvent, activity);
            }

            @Override
            public void onDisable(NamedEvent namedEvent, Activity activity) {
                report(namedEvent, activity);
            }

            private void report(NamedEvent namedEvent, Activity activity) {
                final PumpMessage msg = new PumpMessage(Strings.toString(namedEvent))
                    .withDesc(activity.toString());
                WebReporter.getReporter().info("Window has been " + namedEvent.getName(), msg.toPrettyString());
            }
        };
    }

}
