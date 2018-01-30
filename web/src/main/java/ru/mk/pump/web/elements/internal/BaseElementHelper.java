package ru.mk.pump.web.elements.internal;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import ru.mk.pump.web.configuration.Configuration;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.elements.api.listeners.StateListener;
import ru.mk.pump.web.elements.enums.StateType;

@SuppressWarnings("UnusedReturnValue")
@Slf4j
class BaseElementHelper {

    private final BaseElement baseElement;

    private final Configuration fwConfig;

    BaseElementHelper(BaseElement baseElement) {
        this.baseElement = baseElement;
        this.fwConfig = ConfigurationHolder.get();
    }

    BaseElementHelper addWindowSizeChecker() {
        if (fwConfig.getWindowSizeOffset() >= 0) {
            baseElement.addStateListener(Collections.singletonList(newWindowSizeCheckerStateListener()));
        }
        return this;
    }

    private StateListener newWindowSizeCheckerStateListener() {
        return new StateListener() {
            @Override
            public void onBefore(State state) {
                log.trace("Call StateListener#onBefore from newWindowSizeCheckerStateListener()");
                //do nothing
            }

            @Override
            public void onFinish(State state) {
                if (state.type() == StateType.DISPLAYED && state.result().isSuccess()) {
                    try {
                        int winWidth = baseElement.getBrowser().actions().getSize().getWidth();
                        WebElement avatar = baseElement.getFinder().findFast().throwExceptionOnFail().getResult();
                        int elementWidthPosition = avatar.getLocation().getX() + avatar.getSize().getWidth();
                        baseElement.getVerifier().checkTrue(
                            (String.format("Element size fit the window size. Window width : '%s'. Element position : '%s'", winWidth, elementWidthPosition)),
                            winWidth >= (elementWidthPosition - fwConfig.getWindowSizeOffset()));
                    } catch (Exception ex) {
                        log.error("Cannot check element position in window. Element is '%s'", baseElement.toString());
                    }
                }
            }
        };
    }
}
