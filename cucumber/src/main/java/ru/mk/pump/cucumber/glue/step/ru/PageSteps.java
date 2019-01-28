package ru.mk.pump.cucumber.glue.step.ru;

import static ru.mk.pump.web.common.WebItemsController.cast;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.mk.pump.commons.utils.Pre;
import ru.mk.pump.commons.utils.Strings;
import ru.mk.pump.cucumber.glue.AbstractSteps;
import ru.mk.pump.web.common.AbstractPageItemList;
import ru.mk.pump.web.common.WebItemsController;
import ru.mk.pump.web.component.api.Component;
import ru.mk.pump.web.constants.ElementParams;
import ru.mk.pump.web.elements.ElementList;
import ru.mk.pump.web.elements.api.Element;
import ru.mk.pump.web.elements.api.part.Clickable;
import ru.mk.pump.web.elements.api.part.Editable;
import ru.mk.pump.web.elements.internal.State;
import ru.mk.pump.web.interpretator.items.Method;
import ru.mk.pump.web.page.BasePage;
import ru.mk.pump.web.page.api.Page;

import java.util.List;

@Slf4j
public class PageSteps extends AbstractSteps {

    private final WebItemsController controller;

    public PageSteps() {
        controller = core().getWebController();
    }

    @Given("^(страница|окно) (.+?) открыт[ао] успешно$")
    public void pageSet(String type, String pumpkinExpression) {
        if (isPage(type)) {
            controller.initPage(pumpkinExpression).check();
        } else {
            controller.initComponent(pumpkinExpression).check();
        }
    }

    @Given("^на страницу (.+?) (?:совершен|выполнен) переход( и она открылась успешно|)$")
    public void pageOpen(String pumpkinExpression, boolean check) {
        final Page page = controller.initPage(pumpkinExpression);
        page.open();
        if (check) {
            page.check();
        }
    }

    @Given("^(на странице|в окне) весь текст сохранен в (.+?|result)$")
    public void pageText(String type, String key) {
        final Component object;
        if (isPage(type)) {
            object = controller.getPageManager().getCurrent();
        } else {
            object = controller.getComponentManager().getCurrent();
        }
        Pre.checkObjectNotNull(object, BasePage.class, "Сначала инициализируйте страницу или окно");
        core().getTestVariables().put(key, object.getText());
    }

    @Given("^(на странице|в окне) выполнена команда '(.+?)'(?: результат сохранен в (.+?|result))$")
    public void pageExecute(String type, String pumpkinExpression, String key) {
        final Object object = getWebItem(type, pumpkinExpression);
        if (!Strings.isBlank(key)) {
            core().getTestVariables().put(key, object);
        }
    }

    @Given("^(на странице|в окне) найден элемент '(.+?)' и (?:установлено|выбрано) значение '(.+?)'$")
    public void pageElementSet(String type, String pumpkinExpression, String argument) {
        final Object object = getWebItem(type, pumpkinExpression);
        cast(object, Editable.class).set(ElementParams.EDITABLE_SET_STRING.withValue(argument));
    }

    @Given("^(на странице|в окне) найден элемент '(.+?)' и кол-во подэлементов сохранено в (.+?|result)$")
    public void pageElementListSize(String type, String pumpkinExpression, String key) {
        final Object object = getWebItem(type, pumpkinExpression);
        core().getTestVariables().put(key, cast(object, List.class).size());
    }

    @Given("^(на странице|в окне) найден элемент '(.+?)'(?: текст сохранен в (.+?|result))$")
    public void pageElementText(String type, String pumpkinExpression, String key) {
        final Object object = getWebItem(type, pumpkinExpression);
        core().getTestVariables().put(key, cast(object, Element.class).getText());
    }

    @Given("^(на странице|в окне) найден элемент '(.+?)' и совершен (клик|скролл)")
    public void pageElementClick(String type, String pumpkinExpression, String action) {
        final Object object = getWebItem(type, pumpkinExpression);
        switch (action) {
            case "клик":
                cast(object, Clickable.class).click();
                break;
            case "скролл":
                cast(object, Element.class).scroll();
                break;
            default:
                operationTypeError(action);
        }
    }

    @Given("^(на странице|в окне) найден элемент '(.+?)'( выполнен скролл|) " +
            "и он (виден|не виден|присутствует|не присутствует|включен|не включен)(?: за ([0-9]+?) сек|)$")
    public void pageElementStateDisplayed(String type, String pumpkinExpression, boolean scroll, String status, int timeout) {
        final Object object = getWebItem(type, pumpkinExpression);
        final Element element = cast(object, Element.class);
        final Method method = new Method(status);
        if (timeout > 0) {
            method.addArg(timeout);
        }
        if (scroll) {
            element.scroll();
        }
        final State state = cast(controller.callMethod(element, method), State.class);
        core().getVerifier().checkTrue(state.name(), state.result().isSuccess(), state.toPrettyString());
    }

    //region private
    private static boolean isPage(String string) {
        if (Strings.isBlank(string)) {
            return true;
        }
        return StringUtils.containsIgnoreCase(string, "страниц");
    }

    private Object getWebItem(String type, String pumpkinExpression) {
        if (isPage(type)) {
            return controller.executeOnCurrentPage(pumpkinExpression);
        } else {
            return controller.executeOnCurrentComponent(pumpkinExpression);
        }
    }
    //endregion
}