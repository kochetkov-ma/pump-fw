package ru.mk.pump.cucumber.steps.web;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.cucumber.steps.AbstractSteps;
import ru.mk.pump.cucumber.transform.PParam;
import ru.mk.pump.web.common.WebItemsController;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.Editable;
import ru.mk.pump.web.elements.internal.State;

import static ru.mk.pump.web.common.WebItemsController.cast;

@Slf4j
public class PageSteps extends AbstractSteps {

    private final WebItemsController controller;

    public PageSteps() {
        controller = core().getWebController();
    }

    @Given("^Page - check current page '(.+)'$")
    public void pageSet(String pumpkinExpression) {
        core().getWebController().initPage(pumpkinExpression).check();
    }

    @Given("^Page - open '(.+)'$")
    public void pageOpen(String pumpkinExpression) {
        core().getWebController().initPage(pumpkinExpression).open();
    }

    @Given("^Page - get element '(.+)' and set '(.+)'$")
    public void pageElementSet(String pumpkinExpression, @PParam String argument) {
        cast(controller.executeOnCurrentPage(pumpkinExpression), Editable.class).set(ElementParams.EDITABLE_SET_STRING.withValue(argument));
    }

    @Given("^Page - get element '(.+)' and click")
    public void pageElementClick(String pumpkinExpression) {
        cast(controller.executeOnCurrentPage(pumpkinExpression), Clickable.class).click();
    }

    @Given("^Page - get element '(.+)' and check state 'is displayed'$")
    public void pageElementStateDisplayed(String pumpkinExpression) {
        State res = cast(controller.executeOnCurrentPage(pumpkinExpression), Element.class).isDisplayed();
        core().getVerifier().checkTrue(res.name(), res.result().isSuccess(), res.toPrettyString());
    }

    @Given("^Page - get element '(.+)' and check state 'is NOT displayed'$")
    public void pageElementStateNotDisplayed(String pumpkinExpression) {
        State res = cast(controller.executeOnCurrentPage(pumpkinExpression), Element.class).isNotDisplayed();
        core().getVerifier().checkTrue(res.name(), res.result().isSuccess(), res.toPrettyString());
    }
}