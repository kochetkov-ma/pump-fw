package ru.mk.pump.web.elements.internal;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebElement;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.web.configuration.Configuration;
import ru.mk.pump.web.configuration.ConfigurationHolder;
import ru.mk.pump.web.elements.api.listeners.ActionListener;
import ru.mk.pump.web.elements.api.listeners.StateListener;
import ru.mk.pump.web.elements.enums.StateType;
import ru.mk.pump.web.elements.internal.interfaces.Action;
import ru.mk.pump.web.elements.internal.interfaces.InternalElement;

import java.util.Collections;

@SuppressWarnings("UnusedReturnValue")
@Slf4j
class BaseElementHelper {

    private final BaseElement baseElement;

    private final Configuration fwConfig;

    BaseElementHelper(BaseElement baseElement) {
        this.baseElement = baseElement;
        this.fwConfig = ConfigurationHolder.get();
    }

    BaseElementHelper windowSizeCheckerEnable() {
        if (fwConfig.getElement().getWindowWidthOffset() >= 0) {
            baseElement.addStateListener(Collections.singletonList(newWindowSizeCheckerStateListener()));
        }
        return this;
    }

    public void stateActionReportingEnable() {
        if (fwConfig.getElement().isStateReporting()) {
            baseElement.getActionExecutor().getStateResolver().addListener(newActionStateReporterListener());
        }
    }

    public void stateReportingEnable() {
        if (fwConfig.getElement().isStateReporting()) {
            baseElement.getStateResolver().addListener(newStateReporterListener());
        }
    }

    public void actionsReportingEnable() {
        if (fwConfig.getElement().isActionReporting()) {
            baseElement.getActionExecutor().addListener(newActionReporterListener());
        }
    }

    private ActionListener newActionReporterListener() {
        return new ActionListener() {
            @Override
            public void onAfterActionFail(Action action, Throwable throwable) {
                PumpMessage msg = new PumpMessage("After action was failed")
                    .withPre("Action reporting message")
                    .addExtraInfo(action);
                baseElement.getReporter().warn(Strings.space("Action", action.name(), "in stage", action.getStage().name()), msg.toPrettyString(), throwable);
            }

            @Override
            public void onFinallyStateCheck(Action action) {
                info("After stage check finally block", action, null);
            }

            @Override
            public void onFail(Action action, Throwable throwable) {
                PumpMessage msg = new PumpMessage("Any stage was failed")
                    .withPre("Action reporting message")
                    .addExtraInfo(action);
                baseElement.getReporter().warn(Strings.space("Action", action.name(), "in stage", action.getStage().name()), msg.toPrettyString(), throwable);
            }

            @Override
            public void onSuccess(Action action, Object result) {
                PumpMessage msg = new PumpMessage("All actions is success. Final")
                        .withPre("Action reporting message")
                        .addExtraInfo(action);
                baseElement.getReporter().info(Strings.space("Action",
                        action.name(),
                        "in stage",
                        action.getStage().name()),
                        msg.toPrettyString(),
                        baseElement.getReporter().attachments().text("RESULT", Strings.toString(result)));
            }

            @Override
            public void onBeforeActionSuccess(Action action) {
                PumpMessage msg = new PumpMessage("Before action is success")
                        .withPre("Action reporting message")
                        .addExtraInfo(action);
                baseElement.getReporter().debug(Strings.space("Action", action.name(), "in stage", action.getStage().name()), msg.toPrettyString());
            }

            private void info(String title, Action action, Object result) {
                PumpMessage msg = new PumpMessage(title)
                    .withDesc(Strings.toString(result))
                    .withPre("Action reporting message")
                    .addExtraInfo(action);
                baseElement.getReporter().info(Strings.space("Action", action.name(), "in stage", action.getStage().name()), msg.toPrettyString());
            }
        };
    }

    private StateListener newActionStateReporterListener() {
        return new StateListener() {
            @Override
            public void onBefore(Pair<State, InternalElement> args) {
                if (ConfigurationHolder.get().getElement().isElementHighlight()) {
                    args.getValue().highlight(true);
                }
                //do nothing
            }

            @Override
            public void onFinish(Pair<State, InternalElement> args) {
                PumpMessage msg = new PumpMessage(String
                        .format("Checking state '%s' of element '%s' is finished in '%d' ms with result '%s'", args.getKey().name(),
                                args.getValue().getName(),
                                args.getKey().result().getElapsedTime(),
                                args.getKey().result().isSuccess()))
                        .withPre("State reporting message")
                        .addExtraInfo(args.getKey())
                        .addExtraInfo(args.getValue());
                if (args.getKey().result().isSuccess()) {
                    if (args.getKey().type() == StateType.READY) {
                        baseElement.getReporter().debug(msg.getTitle(), msg.toPrettyString());
                    } else {
                        /*don't report other state*/
                    }
                } else {
                    baseElement.getReporter().warn(msg.getTitle(), msg.toPrettyString());
                }
            }
        };
    }

    private StateListener newStateReporterListener() {
        return new StateListener() {
            @Override
            public void onBefore(Pair<State, InternalElement> args) {
                if (args.getKey().type() == StateType.DISPLAYED || args.getKey().type() == StateType.SELECTED
                        && ConfigurationHolder.get().getElement().isElementHighlight()) {
                    args.getValue().highlight(true);
                }
                log.trace("Call StateListener#onBeforeActionSuccess from newStateReporterListener()");
                //do nothing
            }

            @Override
            public void onFinish(Pair<State, InternalElement> args) {
                PumpMessage msg = new PumpMessage(String
                    .format("Checking state '%s' of element '%s' is finished in '%d' ms with result '%s'", args.getKey().name(),
                        args.getValue().getName(),
                        args.getKey().result().getElapsedTime(),
                        args.getKey().result().isSuccess()))
                    .withPre("State reporting message")
                    .addExtraInfo(args.getKey())
                    .addExtraInfo(args.getValue());
                if (args.getKey().result().isSuccess()) {
                    baseElement.getReporter().info(msg.getTitle(), msg.toPrettyString());
                } else {
                    baseElement.getReporter().warn(msg.getTitle(), msg.toPrettyString());
                }
                if (ConfigurationHolder.get().getElement().isElementHighlight()) {
                    args.getValue().highlight(false);
                }
            }
        };
    }

    private StateListener newWindowSizeCheckerStateListener() {
        return new StateListener() {
            @Override
            public void onBefore(Pair<State, InternalElement> args) {
                log.trace("Call StateListener#onBeforeActionSuccess from newWindowSizeCheckerStateListener()");
                //do nothing
            }

            @Override
            public void onFinish(Pair<State, InternalElement> args) {
                if (args.getKey().type() == StateType.DISPLAYED && args.getKey().result().isSuccess()) {
                    try {
                        int winWidth = baseElement.getBrowser().actions().getSize().getWidth();
                        WebElement avatar = baseElement.getFinder().findFast().throwExceptionOnFail().getResult();
                        int elementWidthPosition = avatar.getLocation().getX() + avatar.getSize().getWidth();
                        baseElement.getVerifier().checkTrue(
                            (String.format("Element size fit the window size. Window width : '%s'. Element position : '%s'", winWidth, elementWidthPosition)),
                            winWidth >= (elementWidthPosition - fwConfig.getElement().getWindowWidthOffset()));
                    } catch (Exception ex) {
                        log.error("Cannot check element position in window. Element is '%s'", baseElement.toString());
                    }
                }
            }
        };
    }
}
