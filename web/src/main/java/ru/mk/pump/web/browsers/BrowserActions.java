package ru.mk.pump.web.browsers;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@SuppressWarnings({"UnusedReturnValue", "unused"})
@Slf4j
public class BrowserActions {

    private final Supplier<WebDriver> driver;

    BrowserActions(@NotNull Supplier<WebDriver> driverSupplier) {

        this.driver = driverSupplier;
    }

    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver.get()).executeScript(script, args);
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

    public String getCurrentUrl(){
        return driver.get().getCurrentUrl();
    }

}
