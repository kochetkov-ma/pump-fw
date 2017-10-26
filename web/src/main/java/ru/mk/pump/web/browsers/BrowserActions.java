package ru.mk.pump.web.browsers;

import java.util.Set;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

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

}
