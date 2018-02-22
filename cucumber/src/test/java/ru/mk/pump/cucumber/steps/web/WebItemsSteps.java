package ru.mk.pump.cucumber.steps.web;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.cucumber.steps.AbstractSteps;

@Slf4j
public class WebItemsSteps extends AbstractSteps {

    @Given("^Page - open '(.+)'$")
    public void openPage(String pumpkinExpression){
        core().getWebController().initPage(pumpkinExpression).open();
    }

}
