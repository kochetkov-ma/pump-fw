package ru.mk.pump.web.browsers;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Step;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import ru.mk.pump.commons.utils.WaitResult;
import ru.mk.pump.commons.utils.Waiter;
import ru.mk.pump.web.exceptions.BrowserException;

import java.util.function.Supplier;

import static ru.mk.pump.commons.utils.Str.format;

@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
@Slf4j
public class BrowserActions {

    private final Supplier<WebDriver> driver;

    BrowserActions(@NonNull Supplier<WebDriver> driverSupplier) {

        this.driver = driverSupplier;
    }

    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver.get()).executeScript(script, args);
    }

    @Deprecated
    public void mobileScroll(int startX, int startY, int stopX, int stopY) {
        TouchAction action = new TouchAction(((AndroidDriver) driver.get()));
        action.longPress(PointOption.point(startX, startY))
                .moveTo(PointOption.point(stopX, stopY))
                .release().perform();
    }

    @Deprecated
    public void mobileScrollDown() {
        MobileDriver mobile = ((MobileDriver) driver.get());
        int startX = (int) (mobile.manage().window().getSize().getWidth() * 0.1);
        int startY = (int) (mobile.manage().window().getSize().getHeight() * 0.8);
        int stopX = 1;
        int stopY = 1;

        mobileScroll(startX, startY, stopX, stopY);
    }

    public void mobileScroll(PointOption startPoint, PointOption stopPoint) {
        TouchAction action = new TouchAction(((AndroidDriver) driver.get()));
        action.longPress(startPoint).moveTo(stopPoint).release().perform();
    }

    public Object executeScript(String script) {
        return ((JavascriptExecutor) driver.get()).executeScript(script);
    }

    public Dimension getSize() {
        return driver.get().manage().window().getSize();
    }

    public String getText(WebElement element) {
        String result = "";
        Exception ex = null;
        try {
            result = element.getText();
            if (result != null && !result.isEmpty()) {
                return result;
            }
        } catch (Exception e) {
            ex = e;
        }
        try {
            result = element.getAttribute("value");
            if (result != null && !result.isEmpty()) {
                return result;
            }
        } catch (Exception e) {
            ex = e;
        }
        try {
            result = element.getAttribute("innerText");
            if (result != null && !result.isEmpty()) {
                return result;
            }
        } catch (Exception e) {
            ex = e;
        }
        if (ex != null) {
            log.error("Cannot get text from element : " + element.toString(), ex);
        }
        return result;
    }

    public String getCurrentUrl() {
        return driver.get().getCurrentUrl();
    }

    public void switchToRootFrame() {
        driver.get().switchTo().defaultContent();
    }

    public void switchToFrame(WebElement element) {
        driver.get().switchTo().frame(element);
    }

    public void switchToFrame(int index) {
        driver.get().switchTo().frame(index);
    }

    @Step("Try to get alert in '{timeout}'")
    public Alert alert(int timeoutS) {
        Waiter waiter = new Waiter();
        WaitResult<Boolean> result = waiter.waitIgnoreExceptions(10, 100, () -> driver.get().switchTo().alert() != null);
        result.throwExceptionOnFail((res) -> new BrowserException()
                .withTitle(format("Waiting of alert has failed in '{}'", res.getTimeout()))
                .withCause(res.getCause())
        );
        return driver.get().switchTo().alert();
    }
}