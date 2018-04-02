package ru.mk.pump.web.elements.internal;

import java.lang.reflect.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.mk.pump.web.component.BaseFrame;

@Slf4j
public class FindFrameStrategy extends SingleElementStrategy {

    public FindFrameStrategy(BaseFrame internalElement) {
        super(internalElement);
    }

    @Override
    public WebElement findSelf() {
        WebElement res = super.findSelf();
        getTarget().getBrowser().actions().switchToFrame(res);
        ((BaseFrame) getTarget()).setActive(true);
        WebElement webElement = (WebElement) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{WebElement.class}, (proxy, method, args) -> {
            if ("findElements".equals(method.getName())) {
                return getTarget().getBrowser().getDriver().findElements((By) args[0]);
            }
            if ("findElement".equals(method.getName())) {
                return getTarget().getBrowser().getDriver().findElement((By) args[0]);
            }
            return method.invoke(res, args);
        });
        getTarget().getFinder().setCache(webElement);
        return webElement;
    }

    @Override
    protected void onException() {
        ((BaseFrame) getTarget()).setActive(false);
    }


}
