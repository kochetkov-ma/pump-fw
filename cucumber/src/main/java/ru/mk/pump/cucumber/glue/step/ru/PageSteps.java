package ru.mk.pump.cucumber.glue.step.ru;

import static ru.mk.pump.web.common.WebItemsController.cast;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.cucumber.glue.AbstractSteps;
import ru.mk.pump.web.common.WebItemsController;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.Editable;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.page.api.Page;

@Slf4j
public class PageSteps extends AbstractSteps {

    private final WebItemsController controller;

    public PageSteps() {
        controller = core().getWebController();
    }

    @Given("^страница открыта успешно (.+?)$")
    public void pageSet(String pumpkinExpression) {
        controller.initPage(pumpkinExpression).check();
    }

    @Given("^страница открыта( и проверена|) (.+?)$")
    public void pageOpen(boolean check, String pumpkinExpression) {
        final Page page = controller.initPage(pumpkinExpression);
        page.open();
        if (check) {
            page.check();
        }
    }

    @Given("^на странице выполнена команда (.+?)(?:результат сохранен в (.+?))$")
    public void pageExecute(String pumpkinExpression, String key) {
        final Object object = controller.executeOnCurrentPage(pumpkinExpression);
        if (!Strings.isBlank(key)) {
            core().getTestVariables().put(key, object);
        }
    }

    @Given("^на странице получен элемент (.+?) и (установлено|выбрано) значение (.+?)$")
    public void pageElementSet(String pumpkinExpression, String argument) {
        cast(controller.executeOnCurrentPage(pumpkinExpression), Editable.class).set(ElementParams.EDITABLE_SET_STRING.withValue(argument));
    }

    @Given("^на странице получен элемент '(.+)' и текст сохранен 'result'$")
    public void pageElementText(String pumpkinExpression) {
        core().getTestVariables().putResult(cast(controller.executeOnCurrentPage(pumpkinExpression), Element.class).getText());
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

    @Given("^Page - get text and save to 'result'$")
    public void pageText() {
        if (controller.getPageManager().getCurrent() == null) {
            throw new IllegalStateException("You must to init any page in controller");
        }
        core().getTestVariables().putResult(controller.getPageManager().getCurrent().getText());
    }
}