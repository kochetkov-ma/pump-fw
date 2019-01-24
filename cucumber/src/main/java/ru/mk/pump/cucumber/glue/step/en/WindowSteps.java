package ru.mk.pump.cucumber.glue.step.en;

import static ru.mk.pump.web.common.WebItemsController.cast;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.cucumber.glue.AbstractSteps;
import ru.mk.pump.web.common.WebItemsController;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.Editable;
import ru.mk.pump.web.elements.internal.State;

@Slf4j
public class WindowSteps extends AbstractSteps {

    private final WebItemsController controller;

    public WindowSteps() {
        controller = core().getWebController();
    }

    @Given("^Window - check current window '(.+)'$")
    public void winSet(String pumpkinExpression) {
        core().getWebController().initComponent(pumpkinExpression).check();
    }

    @Given("^Window - get element '(.+)' and set '(.+)'$")
    public void winElementSet(String pumpkinExpression, String argument) {
        cast(controller.executeOnCurrentComponent(pumpkinExpression), Editable.class).set(ElementParams.EDITABLE_SET_STRING.withValue(argument));
    }

    @Given("^Window - get element '(.+)' and click")
    public void winElementClick(String pumpkinExpression) {
        cast(controller.executeOnCurrentComponent(pumpkinExpression), Clickable.class).click();
    }

    @Given("^Window - get element '(.+)' and save text to 'result'$")
    public void winElementText(String pumpkinExpression) {
        core().getTestVariables().putResult(cast(controller.executeOnCurrentComponent(pumpkinExpression), Element.class).getText());
    }

    @Given("^Window - get element '(.+)' and check state 'is displayed'$")
    public void winElementStateDisplayed(String pumpkinExpression) {
        State res = cast(controller.executeOnCurrentComponent(pumpkinExpression), Element.class).isDisplayed();
        core().getVerifier().checkTrue(res.name(), res.result().isSuccess(), res.toPrettyString());
    }

    @Given("^Window - get element '(.+)' and check state 'is NOT displayed'$")
    public void winElementStateNotDisplayed(String pumpkinExpression) {
        State res = cast(controller.executeOnCurrentComponent(pumpkinExpression), Element.class).isNotDisplayed();
        core().getVerifier().checkTrue(res.name(), res.result().isSuccess(), res.toPrettyString());
    }

    @Given("^Window - execute '(.+)'$")
    public void pageExecute(String pumpkinExpression) {
        core().getWebController().executeOnCurrentComponent(pumpkinExpression);
    }

    @Given("^Window - get text and save to 'result'$")
    public void winText() {
        if (controller.getComponentManager().getCurrent() == null) {
            throw new IllegalStateException("You must to init any component in controller");
        }
        core().getTestVariables().putResult(controller.getComponentManager().getCurrent().getText());
    }
}