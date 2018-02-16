package ru.mk.pump.cucumber.steps.web;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.web.browsers.Browsers;
import ru.mk.pump.web.common.WebItemsController;
import ru.mk.pump.web.page.PageManager;

@Slf4j
public class WebItemsActionSteps {

    @Given("^Page - open '(.+)'$")
    public void openPage(String parameterExpression){
        log.info("Call openPage({})", parameterExpression);
    }

}
